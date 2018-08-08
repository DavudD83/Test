package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class RGB {

    @SerializedName("red")
    @ColumnInfo(name = "red_color")
    private Integer mRed;

    @SerializedName("green")
    @ColumnInfo(name = "green_color")
    private Integer mGreen;

    @SerializedName("blue")
    @ColumnInfo(name = "blue_color")
    private Integer mBlue;

    public RGB(Integer red, Integer green, Integer blue) {
        if(!isCorrectValue(red) || !isCorrectValue(green) || !isCorrectValue(blue)) {
            throw new IllegalArgumentException("Value can not be outside {0:255} range");
        }

        mRed = red;

        mGreen = green;

        mBlue = blue;
    }

    public Integer getRed() {
        return mRed;
    }

    public void setRed(Integer red) {
        mRed = red;
    }

    public Integer getGreen() {
        return mGreen;
    }

    public void setGreen(Integer green) {
        mGreen = green;
    }

    public Integer getBlue() {
        return mBlue;
    }

    public void setBlue(Integer blue) {
        mBlue = blue;
    }

    private boolean isCorrectValue(Integer value) {
        return value >= 0 && value <= 255;
    }
}
