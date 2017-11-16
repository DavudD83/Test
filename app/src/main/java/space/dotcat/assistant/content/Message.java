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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        mEvent = event;
    }

    public Body getBody() {
        return mBody;
    }

    public void setBody(Body body) {
        mBody = body;
    }
}
