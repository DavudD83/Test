package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.SeekBar;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;


public class DimmableLightHolder extends LightHolder {

    @BindView(R.id.sb_light_brightness)
    public SeekBar mBrightnessLevel;

    public DimmableLightHolder(View itemView, RoomDetailsAdapter.LampSwitchClickListener switchStateListener,
                               RoomDetailsAdapter.BrightnessLevelListener brightnessLevelListener) {
        super(itemView, switchStateListener);

        mBrightnessLevel.setOnSeekBarChangeListener(brightnessLevelListener);
    }

    @Override
    public void bind(Lamp colorTemperatureLamp) {
        super.bind(colorTemperatureLamp);

        DimmableLamp dimmableLamp = (DimmableLamp) colorTemperatureLamp;

        mBrightnessLevel.setTag(getAdapterPosition());

        mBrightnessLevel.setProgress(dimmableLamp.getBrightness());
    }
}
