package space.dotcat.assistant.content;

import com.google.gson.JsonElement;

public class WebSocketSubscribeMessage extends WebSocketMessage {

    public WebSocketSubscribeMessage(JsonElement body) {
        super(body);

        mTopic = "subscribe";

        mType = "control";
    }
}
