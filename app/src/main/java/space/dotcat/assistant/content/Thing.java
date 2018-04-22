package space.dotcat.assistant.content;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Things")
@TypeConverters(StringsConverter.class)
public class Thing {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "thing_id")
    @NonNull
    private String mId;

    @SerializedName("capabilities")
    @ColumnInfo(name = "thing_capabilities")
    private List<String> mCapabilities;

    @SerializedName("commands")
    @ColumnInfo(name = "thing_commands")
    private List<String> mCommands;

    @SerializedName("is_active")
    @ColumnInfo(name = "thing_is_active")
    private Boolean mIsActive;

    @SerializedName("is_available")
    @ColumnInfo(name = "thing_is_available")
    private Boolean mIsAvailable;

    @SerializedName("is_enabled")
    @ColumnInfo(name = "thing_is_enabled")
    private Boolean mIsEnabled;

    @SerializedName("placement")
    @ColumnInfo(name = "thing_placement")
    private String mPlacement;

    @SerializedName("state")
    @ColumnInfo(name = "thing_state")
    private String mState;

    @SerializedName("type")
    @ColumnInfo(name = "thing_type")
    private String mType;

    @SerializedName("friendly_name")
    @ColumnInfo(name = "thing_friendly_name")
    private String mFriendlyName;

    @Ignore
    public Thing() {
    }

    public Thing(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                 @NonNull Boolean isActive, @NonNull Boolean isAvailable, @NonNull Boolean isEnabled,
                 @NonNull String placement, @NonNull String state, @NonNull String type,
                 @NonNull String friendlyName) {
        mId = id;
        mCapabilities = capabilities;
        mCommands = commands;
        mIsActive = isActive;
        mIsAvailable = isAvailable;
        mIsEnabled = isEnabled;
        mPlacement = placement;
        mState = state;
        mType = type;
        mFriendlyName = friendlyName;
    }

    @NonNull
    public List<String> getCapabilities() {
        return mCapabilities;
    }

    @NonNull
    public List<String> getCommands() {
        return mCommands;
    }

    @NonNull
    public String getId() { return mId; }

    @NonNull
    public Boolean getIsActive() { return mIsActive; }

    @NonNull
    public Boolean getIsAvailable() { return  mIsAvailable; }

    @NonNull
    public String getPlacement() { return mPlacement; }

    @NonNull
    public String getState() { return mState; }

    @NonNull
    public String getType() { return mType; }

    @NonNull
    public Boolean getIsEnabled() {
        return mIsEnabled;
    }

    @NonNull
    public String getFriendlyName() {
        return mFriendlyName;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setActive(Boolean active) {
        mIsActive = active;
    }

    public void setCommands(List<String> commands) {
        mCommands = commands;
    }

    public void setCapabilities(List<String> capabilities) {
        mCapabilities = capabilities;
    }

    public void setAvailable(Boolean available) {
        mIsAvailable = available;
    }

    public void setPlacement(String placement) {
        mPlacement = placement;
    }

    public void setState(String state) {
        mState = state;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setEnabled(Boolean enabled) {
        mIsEnabled = enabled;
    }

    public void setFriendlyName(String friendlyName) {
        mFriendlyName = friendlyName;
    }
}
