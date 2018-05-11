package space.dotcat.assistant.webSocket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import space.dotcat.assistant.content.AuthBody;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.SubscribeBody;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.WebSocketAuthMessage;
import space.dotcat.assistant.content.WebSocketMessage;
import space.dotcat.assistant.content.WebSocketSubscribeMessage;
import space.dotcat.assistant.repository.authRepository.AuthRepository;

public class WebSocketServiceImpl implements WebSocketService {

    private static final int NORMAL_CLOSE_CODE = 1000;

    private AuthRepository mAuthRepository;

    private OkHttpClient mOkHttpClient;

    private WebSocket mWebSocket;

    private ServerListener mServerListener;

    private Gson mGsonConverter;

    public interface ServerListener {
        void onMessage(WebSocketMessage message);

        void onError(Throwable throwable);
    }

    public WebSocketServiceImpl(AuthRepository authRepository, OkHttpClient okHttpClient,
                                ServerListener serverListener, Gson gsonConverter) {
        mAuthRepository = authRepository;

        mOkHttpClient = okHttpClient;

        mServerListener = serverListener;

        mGsonConverter = gsonConverter;
    }

    @Override
    public void connect() {
        String streaming_url = mAuthRepository.getStreamingUrl();

        Request request = new Request.Builder()
                .url(streaming_url)
                .build();

        mWebSocket = mOkHttpClient.newWebSocket(request, new SimpleWebSocketListener());

        sendAuthMessage();

        subscribeOnTopics();
    }

    private void subscribeOnTopics() {
        SubscribeBody subscribeBody = new SubscribeBody("things/#", false);

        JsonElement jsonBody = mGsonConverter.toJsonTree(subscribeBody);

        WebSocketMessage webSocketMessage = new WebSocketSubscribeMessage(jsonBody);

        String json = mGsonConverter.toJson(webSocketMessage);

        sendMessage(json);
    }

    @Override
    public void disconnect() {
        mWebSocket.close(NORMAL_CLOSE_CODE, "reason");

        mWebSocket = null;
    }

    @Override
    public void sendMessage(String message) {
        mWebSocket.send(message);
    }

    private void sendAuthMessage() {
        String token = mAuthRepository.getToken();

        AuthBody authBody = new AuthBody(token);

        JsonElement jsonBody = mGsonConverter.toJsonTree(authBody);

        WebSocketAuthMessage webSocketAuthInfo = new WebSocketAuthMessage(jsonBody);

        String json = mGsonConverter.toJson(webSocketAuthInfo, WebSocketAuthMessage.class);

        mWebSocket.send(json);
    }

    private class SimpleWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            WebSocketMessage message = mGsonConverter.fromJson(text, WebSocketMessage.class);

            mServerListener.onMessage(message);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            mServerListener.onError(t);

            disconnect();
        }
    }

}
