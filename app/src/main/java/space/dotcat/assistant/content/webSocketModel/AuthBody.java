package space.dotcat.assistant.content.webSocketModel;

import com.google.gson.annotations.SerializedName;

public class AuthBody {

    @SerializedName("access_token")
    private String mAccessToken;

    public AuthBody(String accessToken) {
        mAccessToken = accessToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }
}
