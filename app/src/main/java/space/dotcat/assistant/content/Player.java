package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Players")
public class Player extends Thing {

    @SerializedName("state")
    @ColumnInfo(name = "player_state")
    private String mState;

    @Ignore
    public Player() {
    }

    public Player(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                  @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                  @NonNull String type, @NonNull String friendlyName, @NonNull String state) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName);

        mState = state;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }
}
