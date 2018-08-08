package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.Button;

import com.skydoves.colorpickerpreference.ColorPickerDialog;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.RGB;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;

public class ColorLightHolder extends ColorTemperatureLightHolder {

    @BindView(R.id.bt_chooseColor)
    Button mChooseColorButton;

    @BindView(R.id.view_picked_color)
    View mPickedColor;

    private AlertDialog mColorPickerDialog;

    private RoomDetailsAdapter.ColorListener mColorListener;

    public ColorLightHolder(View itemView, RoomDetailsAdapter.LampSwitchClickListener switchStateListener,
                            RoomDetailsAdapter.BrightnessLevelListener brightnessLevelListener,
                            RoomDetailsAdapter.ColorTemperatureLevelListener colorTemperatureLevelListener,
                            RoomDetailsAdapter.ColorListener colorListener) {
        super(itemView, switchStateListener, brightnessLevelListener, colorTemperatureLevelListener);

        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(itemView.getContext());

        mColorListener = colorListener;

        builder.setTitle(itemView.getContext().getString(R.string.pick_color_label));
        builder.setPreferenceName("Color");
        builder.setPositiveButton("Set color", colorEnvelope -> {
            mPickedColor.setBackgroundColor(colorEnvelope.getColor());

            float [] hsv = new float[3];

            Color.colorToHSV(colorEnvelope.getColor(), hsv);

            mColorListener.onColorChange(getAdapterPosition(), hsv);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        mColorPickerDialog = builder.create();

        mChooseColorButton.setOnClickListener(v-> mColorPickerDialog.show());
    }

    @Override
    public void bind(Lamp lamp) {
        RGBLamp rgbLamp = (RGBLamp) lamp;

        super.bind(rgbLamp);

        mChooseColorButton.setTag(getAdapterPosition());

        float [] hsv = new float[3];

        hsv[0] = rgbLamp.getColorHue();
        hsv[1] = rgbLamp.getColorSaturation();
        hsv[2] = rgbLamp.getBrightness();

        int color = Color.HSVToColor(hsv);

        mPickedColor.setBackgroundColor(color);
    }
}
