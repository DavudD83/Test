package space.dotcat.assistant.content;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AuthorizationAnswer extends RealmObject {

    @SerializedName("message")
    private String mMessage;

    @SerializedName("token")
    private String mToken;


    public AuthorizationAnswer() {
    }

    public AuthorizationAnswer(String message, String token) {
        mMessage = message;
        mToken = token;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }
}
