package space.dotcat.assistant.screen.roomDetail;

import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.CommandArgs;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.PausablePlayer;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.screen.general.BaseRxPresenter;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.PausablePlayerHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.PlayerHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.TrackPlayerHolder;

public class RoomDetailsPresenter extends BaseRxPresenter {

    private final RoomDetailsViewContract mRoomDetailsViewContract;

    private final ThingRepository mThingRepository;

    public RoomDetailsPresenter(@NonNull RoomDetailsViewContract roomDetailsViewContract,
                                @NonNull ThingRepository thingRepository) {
        mRoomDetailsViewContract = roomDetailsViewContract;

        mThingRepository = thingRepository;
    }

    public void init(@NonNull String id) {
        Disposable things = mThingRepository.getThingsById(id)
                .doOnSubscribe(disposable -> mRoomDetailsViewContract.showLoading())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thingList -> {
                            mRoomDetailsViewContract.hideLoading();

                            if (thingList.isEmpty())
                                mRoomDetailsViewContract.showEmptyThingsError();
                            else
                                mRoomDetailsViewContract.showThings(thingList);
                        },

                        throwable -> {
                            mRoomDetailsViewContract.hideLoading();

                            mRoomDetailsViewContract.showError(throwable);
                        });

        mCompositeDisposable.add(things);
    }

    public void reloadThings(@NonNull String id) {
        Disposable things = mThingRepository.refreshThings(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listOfThings -> { },

                        mRoomDetailsViewContract::showError);

        mCompositeDisposable.add(things);
    }

    public void onItemChange(@NonNull Thing thing) {
        Message message = new Message("toggle", new JsonObject());

        String thingId = thing.getId();

        Disposable responseMessage = mThingRepository.doAction(thingId, message)
                .doOnSubscribe(disposable -> mRoomDetailsViewContract.showLoading())
                .doAfterTerminate(mRoomDetailsViewContract::hideLoading)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseActionMessage -> {
                            if(thing instanceof Lamp) {
                                if (((Lamp) thing).getIsActive())
                                    ((Lamp) thing).setActive(false);
                                else
                                    ((Lamp) thing).setActive(true);
                            }

                            updateThing(thing);
                        },
                        throwable -> {
                            if(thing instanceof Lamp) {
                                if (((Lamp) thing).getIsActive())
                                    ((Lamp) thing).setActive(false);
                                else
                                    ((Lamp) thing).setActive(true);
                            }

                            updateThing(thing);

                            mRoomDetailsViewContract.showError(throwable);
                        });

        mCompositeDisposable.add(responseMessage);
    }

    private void updateThing(Thing thing) {
        Disposable disposable = mThingRepository.updateThing(thing)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();

        mCompositeDisposable.add(disposable);
    }

    public void onLampSwitchClick(Lamp lamp) {
        String command;

        Lamp newLamp = (Lamp) lamp.clone();

        if (lamp.getIsPoweredOn()) {
            newLamp.setPoweredOn(false);

            command = "off";
        } else {
            newLamp.setPoweredOn(true);

            command = "on";
        }

        onThingChanged(lamp, newLamp, command);
    }

    public void onDoorSwitchClick(DoorLock doorLock) {
        String command;

        DoorLock newDoorLock = (DoorLock) doorLock.clone();

        if (doorLock.getIsActive()) {
            newDoorLock.setIsActive(false);

            command = "deactivate";
        } else {
            newDoorLock.setIsActive(true);

            command = "activate";
        }

        onThingChanged(doorLock, newDoorLock, command);
    }

    public void onBrightnessLevelChange(DimmableLamp oldLamp, DimmableLamp newLamp) {
        String command = "set_brightness";

        onThingChanged(oldLamp, newLamp, command);
    }

    public void onColorTemperatureLevelChange(ColorTemperatureLamp oldLamp, ColorTemperatureLamp newLamp) {
        String command = "set_color_temp";

        onThingChanged(oldLamp, newLamp, command);
    }

    public void onColorChange(RGBLamp oldValue, RGBLamp newValue) {
        String command = "set_color";

        onThingChanged(oldValue, newValue, command);
    }

    public void onThingChanged(Thing oldThing, Thing newThing, String command) {
        String thingId = newThing.getId();

        Message message = createMessage(command, newThing);

        Disposable disposable = mThingRepository
                .doAction(thingId, message)
                .doOnSubscribe(disposable1 -> mRoomDetailsViewContract.showLoading())
                .doAfterTerminate(mRoomDetailsViewContract::hideLoading)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseActionMessage -> updateThing(newThing),

                        throwable -> {
                            updateThing(oldThing);

                            mRoomDetailsViewContract.showError(throwable);
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    private<T extends Thing> Message createMessage(String command, T item) {
        Message message = new Message();

        if (command.equals("on") || command.equals("off")) {
            JsonElement commandArgs = new JsonObject();

            message = new Message(command, commandArgs);
        } else if (command.equals("set_brightness")) {
            DimmableLamp dimmableLamp = (DimmableLamp) item;

            JsonObject body = new JsonObject();
            body.addProperty("brightness", dimmableLamp.getBrightness());

            message = new Message(command, body);
        } else if (command.equals("set_color_temp")) {
            ColorTemperatureLamp colorTemperatureLamp = (ColorTemperatureLamp) item;

            JsonObject body = new JsonObject();
            body.addProperty("color_temp", colorTemperatureLamp.getColorTemperature());

            message = new Message(command, body);
        } else if (command.equals("activate") || command.equals("deactivate")) {
            JsonElement commandArgs = new JsonObject();

            message = new Message(command, commandArgs);
        } else if (command.equals("set_color")) {
            RGBLamp rgbLamp = (RGBLamp) item;

            JsonObject body = new JsonObject();
            body.addProperty("hue", rgbLamp.getColorHue());
            body.addProperty("saturation", rgbLamp.getColorSaturation());

            message = new Message(command, body);
        } else if (command.equals(TrackPlayerHolder.COMMAND_NEXT) || command.equals(TrackPlayerHolder.COMMAND_PREVIOUS)
                || command.equals(PlayerHolder.PLAYING_COMMAND) || command.equals(PlayerHolder.STOPPED_COMMAND)
                || command.equals(PausablePlayerHolder.PAUSED_COMMAND)) {

            JsonElement body = new JsonObject();

            message = new Message(command, body);
        }

        return message;
    }
}
