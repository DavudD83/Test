package space.dotcat.assistant.content;

import com.google.gson.annotations.SerializedName;

public class SubscribeBody {

    @SerializedName("target_topic")
    private String targetTopic;

    @SerializedName("retain_messages")
    private boolean messagesRetained;

    public SubscribeBody(String targetTopic, boolean messagesRetained) {
        this.targetTopic = targetTopic;

        this.messagesRetained = messagesRetained;
    }

    public String getTargetTopic() {
        return targetTopic;
    }

    public void setTargetTopic(String targetTopic) {
        this.targetTopic = targetTopic;
    }

    public boolean isMessagesRetained() {
        return messagesRetained;
    }

    public void setMessagesRetained(boolean messagesRetained) {
        this.messagesRetained = messagesRetained;
    }
}
