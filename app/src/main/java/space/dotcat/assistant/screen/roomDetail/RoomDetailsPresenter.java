package space.dotcat.assistant.screen.roomDetail;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.assistant.content.CommandArgs;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.screen.general.BaseRxPresenter;

public class RoomDetailsPresenter extends BaseRxPresenter {

    private final RoomDetailsViewContract mRoomDetailsViewContract;

    private final ThingRepository mThingRepository;

    public RoomDetailsPresenter(@NonNull RoomDetailsViewContract roomDetailsViewContract,
                                @NonNull ThingRepository thingRepository) {
        mRoomDetailsViewContract = roomDetailsViewContract;

        mThingRepository = thingRepository;
    }

    public void init(@NonNull String id) {
        Disposable things = mThingRepository
                .getThingsById(id)
                .doOnSubscribe(disposable -> mRoomDetailsViewContract.showLoading())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
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
        Disposable things = mThingRepository
                .refreshThings(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listOfThings -> { },

                        mRoomDetailsViewContract::showError);

        mCompositeDisposable.add(things);
    }

    public void onItemChange(@NonNull Thing thing) {
        CommandArgs commandArgs = new CommandArgs();

        Message message = new Message("toggle", commandArgs);

        String thingId = thing.getId();

        Disposable responseMessage = mThingRepository
                .doAction(thingId, message)
                .doOnSubscribe(disposable -> mRoomDetailsViewContract.showLoading())
                .doAfterTerminate(mRoomDetailsViewContract::hideLoading)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseActionMessage -> {
                            if (thing.getIsActive())
                                thing.setActive(false);
                            else
                                thing.setActive(true);

                            updateThing(thing);
                        },
                        throwable -> {
                            updateThing(thing);

                            mRoomDetailsViewContract.showError(throwable);
                        });

        mCompositeDisposable.add(responseMessage);
    }

    private void updateThing(Thing thing) {
        Disposable disposable = mThingRepository.updateThing(thing)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        mCompositeDisposable.add(disposable);
    }
}
