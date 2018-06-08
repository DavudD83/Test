package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Track_Players")
public class TrackPlayer extends PausablePlayer {

    @ColumnInfo(name = "track_info")
    @SerializedName("track_info")
    private String mTrackInfo;

    public TrackPlayer(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                       @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                       @NonNull String type, @NonNull String friendlyName, @NonNull String state, @NonNull String trackInfo) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName, state);

        mTrackInfo = trackInfo;
    }

    public String getTrackInfo() {
        return mTrackInfo;
    }

    public void setTrackInfo(String trackInfo) {
        mTrackInfo = trackInfo;
    }
}
