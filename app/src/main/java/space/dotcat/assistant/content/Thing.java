package space.dotcat.assistant.content;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import space.dotcat.assistant.utils.TextUtils;

@Entity(tableName = "Things")
@TypeConverters(StringsConverter.class)
public class Thing implements Cloneable {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "thing_id")
    @NonNull
    private String mId;

    @SerializedName("type")
    @ColumnInfo(name = "thing_type")
    private String mType;

    @SerializedName("placement")
    @ColumnInfo(name = "thing_placement")
    private String mPlacement;

    @SerializedName("friendly_name")
    @ColumnInfo(name = "thing_friendly_name")
    private String mFriendlyName;

    @SerializedName("is_enabled")
    @ColumnInfo(name = "thing_is_enabled")
    private Boolean mIsEnabled;

    @SerializedName("is_available")
    @ColumnInfo(name = "thing_is_available")
    private Boolean mIsAvailable;

    @SerializedName("capabilities")
    @ColumnInfo(name = "thing_capabilities")
    private List<String> mCapabilities;

    @SerializedName("commands")
    @ColumnInfo(name = "thing_commands")
    private List<String> mCommands;

    @Ignore
    public Thing() {
    }

    public Thing(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                 @NonNull Boolean isAvailable, @NonNull Boolean isEnabled,
                 @NonNull String placement, @NonNull String type,
                 @NonNull String friendlyName) {
        mId = id;

        mCapabilities = capabilities;

        mCommands = commands;

        mIsAvailable = isAvailable;

        mIsEnabled = isEnabled;

        mPlacement = placement;

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
    public Boolean getIsAvailable() { return  mIsAvailable; }

    @NonNull
    public String getPlacement() { return mPlacement; }

    @NonNull
    public String getType() { return mType; }

    @NonNull
    public Boolean getIsEnabled() {
        return mIsEnabled;
    }

    @NonNull
    public String getFriendlyName() {
        if (TextUtils.isEmpty(mFriendlyName)) {
            mFriendlyName = mType + " in the " + mPlacement;
        }

        return mFriendlyName;
    }

    public void setId(String id) {
        mId = id;
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

    public void setType(String type) {
        mType = type;
    }

    public void setEnabled(Boolean enabled) {
        mIsEnabled = enabled;
    }

    public void setFriendlyName(String friendlyName) {
        mFriendlyName = friendlyName;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
