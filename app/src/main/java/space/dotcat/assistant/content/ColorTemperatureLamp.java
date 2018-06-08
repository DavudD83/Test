package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Color_Temperature_Lamps")
public class ColorTemperatureLamp extends DimmableLamp {

    @SerializedName("color_temp")
    @ColumnInfo(name = "color_temperature")
    private Integer mColorTemperature;


    public ColorTemperatureLamp(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                                @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                                @NonNull String type, @NonNull String friendlyName, @NonNull String state,
                                @NonNull Boolean isPoweredOn, @NonNull Boolean isActive, Integer brightness,
                                @NonNull Integer colorTemperature) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName, state, isPoweredOn, isActive, brightness);

        mColorTemperature = colorTemperature;
    }

    public Integer getColorTemperature() {
        return mColorTemperature;
    }

    public void setColorTemperature(Integer colorTemperature) {
        mColorTemperature = colorTemperature;
    }
}
