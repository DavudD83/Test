package space.dotcat.assistant.content;


import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("type")
    private String mType;

    @SerializedName("source")
    private String mSource;

    @SerializedName("event")
    private String mEvent;

    @SerializedName("body")
    private Body mBody;


    public Message(Body body) {

        mType = "user_request";

        mSource = "android";

        mEvent = "action_requested";

        mBody = body;
    }
}
