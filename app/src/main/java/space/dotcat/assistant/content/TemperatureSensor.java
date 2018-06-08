package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "Temperature_Sensors")
public class TemperatureSensor extends Thing {

    @ColumnInfo(name = "temperature_celsius")
    private Float mTemperatureC;

    public TemperatureSensor(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                             @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                             @NonNull String type, @NonNull String friendlyName, @NonNull Float temperatureC) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName);

        mTemperatureC = temperatureC;
    }

    public Float getTemperatureC() {
        return mTemperatureC;
    }

    public void setTemperatureC(Float temperatureC) {
        mTemperatureC = temperatureC;
    }
}
