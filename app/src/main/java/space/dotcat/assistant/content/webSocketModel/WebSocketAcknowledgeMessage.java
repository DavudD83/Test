package space.dotcat.assistant.content.webSocketModel;

import com.google.gson.JsonElement;

public class WebSocketAcknowledgeMessage extends WebSocketMessage {

    public WebSocketAcknowledgeMessage(JsonElement body) {
        super(body);

        mType = "control";

        mTopic = "delivery_ack";
    }
}
