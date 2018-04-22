package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "Rooms")
public class Room implements Parcelable {

    @SerializedName("friendly_name")
    @ColumnInfo(name = "room_friendly_name")
    private String mFriendlyName;

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "room_id")
    @NonNull
    private String mId;

    @SerializedName("image_url")
    @ColumnInfo(name = "room_image_path")
    private String mImagePath;

    @Ignore
    public Room() {
    }

    public Room(@NonNull String friendlyName, @NonNull String id, @NonNull String imagePath) {
        mFriendlyName = friendlyName;

        mId = id;

        mImagePath = imagePath;
    }

    @Ignore
    protected Room(Parcel in) {
        mFriendlyName = in.readString();
        mId = in.readString();
        mImagePath = in.readString();
    }

    @Ignore
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
    public String getId() {
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
