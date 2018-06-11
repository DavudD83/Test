package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Contact_Sensors")
public class ContactSensor extends Thing {

    @SerializedName("value")
    @ColumnInfo(name = "value")
    private Integer mValue;

    @SerializedName("is_active")
    @ColumnInfo(name = "is_active")
    private Boolean mIsActive;

    @SerializedName("state")
    @ColumnInfo(name = "state")
    private String mState;

    public ContactSensor(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                         @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                         @NonNull String type, @NonNull String friendlyName, Integer value, Boolean isActive,
                         String state) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName);
        mValue = value;

        mIsActive = isActive;

        mState = state;
    }

    public Integer getValue() {
        return mValue;
    }

    public void setValue(Integer value) {
        mValue = value;
    }

    public Boolean getIsActive() {
        return mIsActive;
    }

    public void setIsActive(Boolean active) {
        mIsActive = active;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }
}
