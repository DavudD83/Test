package space.dotcat.assistant.content;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "Pausable_Players")
public class PausablePlayer extends Player {

    public PausablePlayer(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                          @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                          @NonNull String type, @NonNull String friendlyName, @NonNull String state) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName, state);
    }
}
