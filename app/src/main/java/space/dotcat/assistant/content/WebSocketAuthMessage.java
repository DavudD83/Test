package space.dotcat.assistant.content;

import com.google.gson.JsonElement;

public class WebSocketAuthMessage extends WebSocketMessage {

    private static final String TYPE = "control";

    private static final String TOPIC = "auth";

    public WebSocketAuthMessage(JsonElement body) {
        super(body);

        mType = TYPE;

        mTopic = TOPIC;
    }
}
