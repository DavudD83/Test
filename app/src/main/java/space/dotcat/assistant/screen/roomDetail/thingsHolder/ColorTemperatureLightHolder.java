package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.SeekBar;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;

public class ColorTemperatureLightHolder extends DimmableLightHolder {

    @BindView(R.id.sb_light_color_temperature)
    public SeekBar mColorTemperature;

    public ColorTemperatureLightHolder(View itemView, RoomDetailsAdapter.LampSwitchClickListener switchStateListener,
                                       RoomDetailsAdapter.BrightnessLevelListener brightnessLevelListener,
                                       RoomDetailsAdapter.ColorTemperatureLevelListener colorTemperatureLevelListener) {
        super(itemView, switchStateListener, brightnessLevelListener);

        mColorTemperature.setOnSeekBarChangeListener(colorTemperatureLevelListener);
    }

    @Override
    public void bind(Lamp lamp) {
        ColorTemperatureLamp colorTemperatureLamp = (ColorTemperatureLamp) lamp;

        super.bind(colorTemperatureLamp);

        mColorTemperature.setTag(getAdapterPosition());

        mColorTemperature.setProgress(colorTemperatureLamp.getColorTemperature());
    }
}
