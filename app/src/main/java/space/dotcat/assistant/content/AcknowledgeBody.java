package space.dotcat.assistant.content;

import com.google.gson.annotations.SerializedName;

public class AcknowledgeBody {

    @SerializedName("message_id")
    private Integer mMessageId;

    public AcknowledgeBody() {
    }

    public AcknowledgeBody(Integer messageId) {
        mMessageId = messageId;
    }

    public Integer getMessageId() {
        return mMessageId;
    }

    public void setMessageId(Integer messageId) {
        mMessageId = messageId;
    }
}
