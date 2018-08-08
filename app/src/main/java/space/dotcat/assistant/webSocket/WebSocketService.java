package space.dotcat.assistant.webSocket;

public interface WebSocketService {

    void connect();

    void disconnect();

    void sendMessage(String message);
}
