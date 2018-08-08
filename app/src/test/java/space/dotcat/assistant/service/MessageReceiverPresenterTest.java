package space.dotcat.assistant.service;

import android.arch.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import space.dotcat.assistant.base.BasePresenterTest;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.webSocketModel.WebSocketMessage;
import space.dotcat.assistant.notifications.NotificationHandler;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.webSocket.WebSocketService;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MessageReceiverPresenterTest extends BasePresenterTest<MessageReceiverPresenter> {

    @Mock
    private MessageReceiverServiceContract mMessageReceiverServiceContract;

    @Mock
    private LiveData<Boolean> mWiFiListener;

    @Mock
    private WebSocketService mWebSocketService;

    @Mock
    private AuthRepository mAuthRepository;

    @Mock
    private RoomRepository mRoomRepository;

    @Mock
    private ThingRepository mThingRepository;

    @Mock
    private NotificationHandler mNotificationHandler;

    private Gson mGson;

    private static final String WEB_SOCKET_THING_MESSAGE = "{\"body\": {\"commands\": [\"on\", \"off\", \"activate\", \"deactivate\", \"toggle\"], \"is_active\": false, \"is_powered_on\": false, \"is_available\": true, \"id\": \"Li2\", \"capabilities\": [\"actuator\", \"has_state\", \"on_off\", \"is_active\"], \"last_updated\": 1533658011.0424953, \"friendly_name\": \"Dummy light bulb\", \"state\": \"off\", \"placement\": \"R5\", \"type\": \"light\", \"is_enabled\": true}, \"type\": \"data\", \"topic\": \"things/Li2/modified\", \"timestamp\": 1533658011.043048, \"message_id\": 3}";

    private static final String TOKEN = "TOKEN";

    private static final Completable COMPLETABLE = Completable.complete();

    private static final Room ROOM = new Room();

    private static final Maybe<Room> ROOM_MAYBE = Maybe.just(ROOM);

    @Override
    protected MessageReceiverPresenter createPresenterForTesting() {
        mGson = new GsonBuilder().create(); // i am aware that creating gson manually for unit testing is not a good idea, but this is a final class

        return new MessageReceiverPresenter(mMessageReceiverServiceContract, mWiFiListener, mWebSocketService,
                mAuthRepository, mRoomRepository, mThingRepository, mGson, mNotificationHandler);
    }

    @Test
    public void testCreateReceiverPresenter() {
        assertNotNull(mPresenter);
    }

    @Test
    public void testConnectToServer() {
        mPresenter.connectToServer();

        verify(mWebSocketService).connect();
    }

    @Test
    public void testSendAuthMessage() {
        when(mAuthRepository.getToken()).thenReturn(TOKEN);

        mPresenter.sendAuthMessage();

        verify(mAuthRepository).getToken();

        verify(mWebSocketService).sendMessage(any(String.class));
    }

    @Test
    public void testHandleThingResponseMessage() {
        WebSocketMessage webSocketMessage = mGson.fromJson(WEB_SOCKET_THING_MESSAGE, WebSocketMessage.class);

        when(mThingRepository.updateThing(any(Thing.class))).thenReturn(COMPLETABLE);

        when(mRoomRepository.getRoomById("R5")).thenReturn(ROOM_MAYBE);

        mPresenter.handleResponseMessage(webSocketMessage);

        verify(mThingRepository).updateThing((argThat(thing -> thing.getId().equals("Li2"))));

        verify(mRoomRepository).getRoomById("R5");

        verify(mNotificationHandler).sendEventNotification(any(String.class), any(Room.class));
    }

    @Test
    public void testHandleErrorResponseMessage() {
        ApiError apiError = new ApiError();
        apiError.setDevelMessage("ERROR");

        WebSocketMessage webSocketMessage = new WebSocketMessage(mGson.toJsonTree(apiError));
        webSocketMessage.setTopic("error/");

        mPresenter.handleResponseMessage(webSocketMessage);

        verify(mMessageReceiverServiceContract).sendErrorBroadcast(argThat(argument -> argument.getDevelMessage().equals("ERROR")));
    }

    @Test
    public void testSubscribeOnAllTopics() {
        mPresenter.subscribeOnAllTopics();

        verify(mWebSocketService).sendMessage(any(String.class));
    }

    @Test
    public void testSendAcknowledgeMessage() {
        mPresenter.sendAcknowledgeMessage(0);

        verify(mWebSocketService).sendMessage(any(String.class));
    }
}
