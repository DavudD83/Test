package space.dotcat.assistant.content.webSocketModel;

import com.google.gson.JsonElement;

public class WebSocketSubscribeMessage extends WebSocketMessage {

    public WebSocketSubscribeMessage(JsonElement body) {
        super(body);

        mTopic = "subscribe";

        mType = "control";
    }
}
