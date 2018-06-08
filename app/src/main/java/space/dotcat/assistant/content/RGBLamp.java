package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "RGB_Lamps")
public class RGBLamp extends ColorTemperatureLamp {

    @SerializedName("color_rgb")
    @Embedded
    private RGB mColorRGB;

    @SerializedName("color_hue")
    @ColumnInfo(name = "color_hue")
    private Float mColorHue;

    @SerializedName("color_saturation")
    @ColumnInfo(name = "color_saturation")
    private Float mColorSaturation;

    public RGBLamp(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                   @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                   @NonNull String type, @NonNull String friendlyName, @NonNull String state,
                   @NonNull Boolean isPoweredOn, @NonNull Boolean isActive, Integer brightness,
                   @NonNull Integer colorTemperature, @NonNull RGB colorRGB, @NonNull Float colorHue,
                   @NonNull Float colorSaturation) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName, state, isPoweredOn, isActive, brightness, colorTemperature);

        mColorRGB = colorRGB;

        mColorHue = colorHue;

        mColorSaturation = colorSaturation;
    }

    public RGB getColorRGB() {
        return mColorRGB;
    }

    public void setColorRGB(RGB colorRGB) {
        mColorRGB = colorRGB;
    }

    public Float getColorHue() {
        return mColorHue;
    }

    public void setColorHue(Float colorHue) {
        mColorHue = colorHue;
    }

    public Float getColorSaturation() {
        return mColorSaturation;
    }

    public void setColorSaturation(Float colorSaturation) {
        mColorSaturation = colorSaturation;
    }
}
