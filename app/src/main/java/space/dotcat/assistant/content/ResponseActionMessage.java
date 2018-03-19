package space.dotcat.assistant.content;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ResponseActionMessage {

    @SerializedName("message")
    private String mMessage;

    public ResponseActionMessage(@NonNull String message) {
        mMessage = message;
    }

    @NonNull
    public String getMessage() {
        return mMessage;
    }

    public void setMessage(@NonNull String message) {
        mMessage = message;
    }
}
