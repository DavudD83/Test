package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Speakers", inheritSuperIndices = true)
public class Speaker extends Thing {

    @SerializedName("")
    @ColumnInfo(name = "is_powered_on")
    private Boolean mIsPoweredOn;

    @SerializedName("speaker_volume")
    @ColumnInfo(name = "speaker_volume")
    private Integer mVolume;

    public Speaker(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                   @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                   @NonNull String type, @NonNull String friendlyName, @NonNull Boolean isPoweredOn,
                   @NonNull Integer volume) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName);

        mIsPoweredOn = isPoweredOn;

        mVolume = volume;
    }

    public Boolean getIsPoweredOn() {
        return mIsPoweredOn;
    }

    public void setPoweredOn(Boolean poweredOn) {
        mIsPoweredOn = poweredOn;
    }

    public Integer getVolume() {
        return mVolume;
    }

    public void setVolume(Integer volume) {
        mVolume = volume;
    }
}
