package space.dotcat.assistant.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class Room extends RealmObject implements Parcelable {

    @SerializedName("friendly_name")
    private String mFriendlyName;

    @SerializedName("id")
    private String mId;

    @SerializedName("image_url")
    private String mImagePath;

    public Room() {
    }

    public Room(@NonNull String friendlyName, @NonNull String id, @NonNull String imagePath) {
        mFriendlyName = friendlyName;

        mId = id;

        mImagePath = imagePath;
    }

    protected Room(Parcel in) {
        mFriendlyName = in.readString();
        mId = in.readString();
        mImagePath = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @NonNull
    public String GetId() {
        return mId;
    }

    @NonNull
    public String getFriendlyName() {
        return mFriendlyName;
    }

    public void setDescription(String friendlyName) {
        mFriendlyName = friendlyName;
    }

    public void setId(String id) { mId = id; }

    @NonNull
    public String getImagePath() {return mImagePath;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFriendlyName);
        parcel.writeString(mId);
        parcel.writeString(mImagePath);
    }
}
