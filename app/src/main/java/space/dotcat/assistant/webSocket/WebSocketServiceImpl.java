package space.dotcat.assistant.webSocket;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import space.dotcat.assistant.content.webSocketModel.WebSocketMessage;
import space.dotcat.assistant.repository.authRepository.AuthRepository;

public class WebSocketServiceImpl implements WebSocketService {

    private static final int NORMAL_CLOSE_CODE = 1000;

    private static final String CLOSE_REASON = "normal close reason";

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
        if (mWebSocket != null) {
            return;
        }

        synchronized(WebSocketServiceImpl.class) {
            String streaming_url = mAuthRepository.getStreamingUrl();

            Request request = new Request.Builder()
                    .url(streaming_url)
                    .build();

            mWebSocket = mOkHttpClient.newWebSocket(request, new SimpleWebSocketListener());
        }
    }

    @Override
    public void disconnect() {
        if(mWebSocket != null) {
            mWebSocket.close(NORMAL_CLOSE_CODE, CLOSE_REASON);
        }

        mWebSocket = null;
    }

    @Override
    public void sendMessage(String message) {
        if(mWebSocket == null) {
            return;
        }

        mWebSocket.send(message);
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
            mWebSocket = null;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            disconnect();

            mServerListener.onError(t);
        }
    }

}
