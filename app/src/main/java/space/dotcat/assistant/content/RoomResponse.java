package space.dotcat.assistant.content;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoomResponse {

    @SerializedName("placements")
    private List<Room> mRooms = new ArrayList<>();

    public RoomResponse(@NonNull List<Room> rooms) {

        mRooms = rooms;
    }

    @NonNull
    public List<Room> getRooms() { return mRooms; }

    public void setRooms(List<Room> mRooms) {
        mRooms = mRooms;
    }
}
