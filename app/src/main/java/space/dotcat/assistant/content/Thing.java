package space.dotcat.assistant.content;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Thing extends RealmObject{

    @SerializedName("commands")
    private RealmList<RealmString> mActions;

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("is_active")
    private Boolean mIsActive;

    @SerializedName("is_available")
    private Boolean mIsAvailable;

    @SerializedName("is_enabled")
    private Boolean mIsEnabled;

    @SerializedName("placement")
    private String mPlacement;

    @SerializedName("state")
    private String mState;

    @SerializedName("type")
    private String mType;

    @SerializedName("friendly_name")
    private String mFriendlyName;

    public Thing() {
    }

    public Thing(@NonNull RealmList<RealmString> actions, @NonNull String id,
                 @NonNull Boolean isActive, @NonNull Boolean isAvailable, @NonNull String placement,
                 @NonNull String state, @NonNull String type, @NonNull Boolean isEnabled,
                 @NonNull String friendlyName) {
        mActions = actions;

        mId = id;

        mIsActive = isActive;

        mIsAvailable = isAvailable;

        mPlacement = placement;

        mState = state;

        mType = type;

        mIsEnabled = isEnabled;

        mFriendlyName = friendlyName;
    }

    @NonNull
    public RealmList<RealmString> getActions() { return mActions; }

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
    public Boolean getEnabled() {
        return mIsEnabled;
    }

    @NonNull
    public String getFriendlyName() {
        return mFriendlyName;
    }

    public void setActions(RealmList<RealmString> actions) {
        mActions = actions;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setActive(Boolean active) {
        mIsActive = active;
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
