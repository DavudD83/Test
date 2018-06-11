package space.dotcat.assistant.screen.roomDetail;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.PausablePlayer;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.content.RGB;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.TrackPlayer;
import space.dotcat.assistant.screen.general.BaseRecyclerViewAdapter;
import space.dotcat.assistant.screen.general.BaseViewHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.ColorLightHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.ColorTemperatureLightHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.ContactSensorsHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.DimmableLightHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.DoorLockHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.LightHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.PausablePlayerHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.PlayerHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.TemperatureSensorHolder;
import space.dotcat.assistant.screen.roomDetail.thingsHolder.TrackPlayerHolder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RoomDetailsAdapter extends BaseRecyclerViewAdapter<Thing, BaseViewHolder<Thing>> {

    private static final int UNKNOWN_THING = 0;

    private static final int DOOR_ACTUATOR = 1;

    private static final int LAMP = 2;

    private static final int DIMMABLE_LIGHT = 3;

    private static final int COLOR_TEMPERATURE_LIGHT = 4;

    private static final int COLOR_LIGHT = 5;

    private static final int PLAYER = 6;

    private static final int PAUSABLE_PLAYER = 7;

    private static final int TRACK_PLAYER = 8;

    private static final int CONTACT_SENSOR = 9;

    private static final int TEMPERATURE_SENSOR = 10;

    private static final HashMap<String, Integer> mTypes = new LinkedHashMap<>();

    private final OnThingClick mOnThingClickListener;

    static {
        mTypes.put("door_actuator", DOOR_ACTUATOR);

        mTypes.put("light", LAMP);
        mTypes.put("dimmable_light", DIMMABLE_LIGHT);
        mTypes.put("ct_light", COLOR_TEMPERATURE_LIGHT);
        mTypes.put("color_light", COLOR_LIGHT);

        mTypes.put("player", PLAYER);
        mTypes.put("pausable_player", PAUSABLE_PLAYER);
        mTypes.put("track_player", TRACK_PLAYER);

        mTypes.put("contact_sensor", CONTACT_SENSOR);
        mTypes.put("temperature_sensor", TEMPERATURE_SENSOR);
    }

    private final View.OnClickListener mOnClickListener = view -> {
        int position = (int) view.getTag();

        Thing thing = mItems.get(position);

        mOnItemClickListener.onItemClick(thing);
    };

    public RoomDetailsAdapter(List<Thing> items, OnItemClickListener<Thing> OnItemClickListener,
                              OnThingClick onThingClick) {
        super(items, OnItemClickListener);

        mOnThingClickListener = onThingClick;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {
            case DOOR_ACTUATOR: {
                View view = layoutInflater.inflate(R.layout.item_door_lock, parent, false);

                final DoorLockHolder holder = new DoorLockHolder(view, new DoorSwitchClickListener<DoorLock>());

                return holder;
            }

            case LAMP: {
                View view = layoutInflater.inflate(R.layout.item_light, parent, false);

                final LightHolder holder = new LightHolder(view, new LampSwitchClickListener<Lamp>());

                return holder;
            }

            case DIMMABLE_LIGHT: {
                View view = layoutInflater.inflate(R.layout.item_dimmable_light, parent, false);

                final DimmableLightHolder holder = new DimmableLightHolder(view,
                        new LampSwitchClickListener<DimmableLamp>(), new BrightnessLevelListener<DimmableLamp>());

                return holder;
            }

            case COLOR_TEMPERATURE_LIGHT: {
                View view = layoutInflater.inflate(R.layout.item_color_temperature_light, parent, false);

                ColorTemperatureLightHolder holder = new ColorTemperatureLightHolder(view,
                        new LampSwitchClickListener<ColorTemperatureLamp>(), new BrightnessLevelListener<ColorTemperatureLamp>(),
                        new ColorTemperatureLevelListener<ColorTemperatureLamp>());

                return holder;
            }

            case COLOR_LIGHT: {
                View view = layoutInflater.inflate(R.layout.item_color_light, parent, false);

                final ColorLightHolder holder = new ColorLightHolder(view,
                        new LampSwitchClickListener<RGBLamp>(), new BrightnessLevelListener<RGBLamp>(),
                        new ColorTemperatureLevelListener<RGBLamp>(),
                        new ColorListener<RGBLamp>());

                return holder;
            }

            case PLAYER: {
                View view = layoutInflater.inflate(R.layout.item_player, parent, false);

                final PlayerHolder holder = new PlayerHolder(view, new PlayerStateListener<Player>());

                return holder;
            }

            case PAUSABLE_PLAYER: {
                View view = layoutInflater.inflate(R.layout.item_pausable_player, parent, false);

                final PausablePlayerHolder holder = new PausablePlayerHolder(view, new PlayerStateListener<PausablePlayer>());

                return holder;
            }

            case TRACK_PLAYER: {
                View view = layoutInflater.inflate(R.layout.item_track_player, parent, false);

                final TrackPlayerHolder holder = new TrackPlayerHolder(view, new PlayerStateListener<TrackPlayer>(),
                        new PlayerTrackListener<TrackPlayer>());

                return holder;
            }

            case CONTACT_SENSOR: {
                View view = layoutInflater.inflate(R.layout.item_contact_sensor, parent, false);

                final ContactSensorsHolder contactSensorsHolder = new ContactSensorsHolder(view);

                return contactSensorsHolder;
            }

            case TEMPERATURE_SENSOR: {
                View view = layoutInflater.inflate(R.layout.item_temperature_sensor, parent, false);

                final TemperatureSensorHolder temperatureSensorHolder = new TemperatureSensorHolder(view);

                return temperatureSensorHolder;
            }

            case UNKNOWN_THING: {

            }

            default: {
                View item = layoutInflater.inflate(R.layout.item_room_detail, parent, false);

                final RoomDetailsHolder roomDetailsHolder = new RoomDetailsHolder(item);

                roomDetailsHolder.mSwitch.setOnClickListener(mOnClickListener);

                return roomDetailsHolder;
            }
        }
    }

    @Override
    public void updateData(List<Thing> newItems) {
        mItems.clear();
        mItems.addAll(newItems);

        notifyDataSetChanged();
    }

    @Override
    protected DiffUtil.Callback createDiffUtilCallback(List<Thing> oldList, List<Thing> newList) {
        return null; //we manually update entire list via notifyDataSetChanged(), that is why we do not callback in this case
    }

    @Override
    public int getItemViewType(int position) {
        Thing thing = mItems.get(position);

        int type;

        String thingType = thing.getType();

        if (mTypes.containsKey(thingType)) {
            type = mTypes.get(thingType);
        } else {
            type = UNKNOWN_THING;
        }

        return type;
    }

    public interface OnThingClick {

        void onLampSwitchClick(Lamp lamp);

        void onDoorLockSwitchClick(DoorLock value);

        void onBrightnessLevelChange(DimmableLamp oldValue, DimmableLamp newValue);

        void onColorTemperatureLevelChange(ColorTemperatureLamp oldValue, ColorTemperatureLamp newValue);

        void onColorChange(RGBLamp oldValue, RGBLamp newValue);

        void onPlayerChange(Player oldItem, Player newItem, String command);
    }

    public class LampSwitchClickListener<T extends Lamp> implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            T item = (T) mItems.get(position);

            mOnThingClickListener.onLampSwitchClick(item);
        }
    }

    public class DoorSwitchClickListener<T extends DoorLock> implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            T item = (T) mItems.get(position);

            mOnThingClickListener.onDoorLockSwitchClick(item);
        }
    }

    public class BrightnessLevelListener<T extends DimmableLamp> implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int position = (int) seekBar.getTag();

            T oldItem = (T) mItems.get(position);

            T newItem = (T) oldItem.clone();
            newItem.setBrightness(seekBar.getProgress());

            mOnThingClickListener.onBrightnessLevelChange(oldItem, newItem);
        }
    }

    public class ColorTemperatureLevelListener<T extends ColorTemperatureLamp> implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int position = (int) seekBar.getTag();

            T oldItem = (T) mItems.get(position);

            T newItem = (T) oldItem.clone();

            int progress = seekBar.getProgress();

            if (progress < 1000) {
                progress += 1000 - progress;
            }

            newItem.setColorTemperature(progress);

            mOnThingClickListener.onColorTemperatureLevelChange(oldItem, newItem);
        }
    }

    public class ColorListener<T extends RGBLamp> {

        public void onColorChange(int adapterPosition, float[] hsvColor) {
            T oldItem = (T) mItems.get(adapterPosition);

            T newItem = (T) oldItem.clone();

            newItem.setColorHue(hsvColor[0]);
            newItem.setColorSaturation(hsvColor[1] * 255);

            mOnThingClickListener.onColorChange(oldItem, newItem);
        }
    }

    public class PlayerStateListener<T extends Player> {

        public void onPlayerStateChange(int adapterPosition, String state, String command) {
            T oldItem = (T) mItems.get(adapterPosition);

            T newItem = (T) oldItem.clone();
            newItem.setState(state);

            mOnThingClickListener.onPlayerChange(oldItem, newItem, command);
        }
    }

    public class PlayerTrackListener<T extends TrackPlayer> {

        public void onTrackChange(int adapterPosition, String command) {
            T oldItem = (T) mItems.get(adapterPosition);

            T newItem = (T) oldItem.clone();

            mOnThingClickListener.onPlayerChange(oldItem, newItem, command);
        }
    }
}
