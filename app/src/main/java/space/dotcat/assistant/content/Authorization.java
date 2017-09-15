package space.dotcat.assistant.content;


import com.google.gson.annotations.SerializedName;

public class Authorization {

    @SerializedName("username")
    private String mUsername;

    @SerializedName("password")
    private String mPassword;

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
       mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
       mPassword = password;
    }

    public Authorization(String username, String password){
        mUsername = username;
        mPassword = password;
    }
}
