package space.dotcat.assistant.content;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class WebSocketMessage {

    @SerializedName("timestamp")
    protected Float mTimestamp;

    @SerializedName("type")
    protected String mType;

    @SerializedName("topic")
    protected String mTopic;

    @SerializedName("body")
    protected JsonElement mBody;

    @SerializedName("message_id")
    protected Integer mMessageId;

    public WebSocketMessage() {
    }

    public WebSocketMessage(JsonElement body) {
        mTimestamp = System.currentTimeMillis() / 1000F;

        mBody = body;
    }

    public Float getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Float timestamp) {
        mTimestamp = timestamp;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        mTopic = topic;
    }

    public JsonElement getBody() {
        return mBody;
    }

    public void setBody(JsonElement body) {
        mBody = body;
    }

    public Integer getMessageId() {
        return mMessageId;
    }

    public void setMessageId(Integer messageId) {
        mMessageId = messageId;
    }

}
