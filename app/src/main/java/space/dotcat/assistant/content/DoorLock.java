package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Door_Locks")
public class DoorLock extends Thing {

    @SerializedName("state")
    @ColumnInfo(name = "door_state")
    private String mState;

    @SerializedName("is_active")
    @ColumnInfo(name = "door_is_active")
    private Boolean mIsActive;

    @Ignore
    public DoorLock() {
    }

    public DoorLock(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                    @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                    @NonNull String type, @NonNull String friendlyName, String state, Boolean isActive) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName);

        mState = state;

        mIsActive = isActive;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public Boolean getIsActive() {
        return mIsActive;
    }

    public void setIsActive(Boolean active) {
        mIsActive = active;
    }
}
