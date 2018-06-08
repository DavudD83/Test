package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Dimmable_Lamps")
public class DimmableLamp extends Lamp {

    @SerializedName("brightness")
    @ColumnInfo(name = "lamp_brightness")
    private Integer mBrightness;

    @Ignore
    public DimmableLamp() {
    }

    public DimmableLamp(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                        @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                        @NonNull String type, @NonNull String friendlyName, @NonNull String state, @NonNull Boolean isPoweredOn,
                        @NonNull Boolean isActive, Integer brightness) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName, state, isPoweredOn, isActive);

        mBrightness = brightness;
    }

    public Integer getBrightness() {
        return mBrightness;
    }

    public void setBrightness(Integer brightness) {
        mBrightness = brightness;
    }
}
