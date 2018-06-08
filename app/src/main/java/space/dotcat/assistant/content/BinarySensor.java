package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "Binary_Sensors")
public class BinarySensor extends ValueSensor {

    @ColumnInfo(name = "thing_is_active")
    private Boolean mIsActive;

    @Ignore
    public BinarySensor() {
    }

    public BinarySensor(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                        @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                        @NonNull String type, @NonNull String friendlyName, @NonNull String value, @NonNull Boolean isActive) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName, value);

        mIsActive = isActive;
    }

    public Boolean getIsActive() {
        return mIsActive;
    }

    public void setActive(Boolean active) {
        mIsActive = active;
    }
}
