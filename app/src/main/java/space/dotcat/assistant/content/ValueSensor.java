package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "ValueSensors")
public class ValueSensor extends Thing {

    @ColumnInfo(name = "sensor_value")
    private String mValue;

    @Ignore
    public ValueSensor() {
    }

    public ValueSensor(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                       @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                       @NonNull String type, @NonNull String friendlyName, @NonNull String value) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName);

        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
