package space.dotcat.assistant.service;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.webSocketModel.AcknowledgeBody;
import space.dotcat.assistant.content.webSocketModel.AuthBody;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.webSocketModel.SubscribeBody;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.webSocketModel.WebSocketAcknowledgeMessage;
import space.dotcat.assistant.content.webSocketModel.WebSocketAuthMessage;
import space.dotcat.assistant.content.webSocketModel.WebSocketMessage;
import space.dotcat.assistant.content.webSocketModel.WebSocketSubscribeMessage;
import space.dotcat.assistant.notifications.NotificationHandler;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.screen.general.BaseRxPresenter;
import space.dotcat.assistant.webSocket.WebSocketService;

public class MessageReceiverPresenter extends BaseRxPresenter {

    private MessageReceiverServiceContract mMessageReceiverServiceContract;

    private LiveData<Boolean> mWiFiReceiver;

    private WebSocketService mWebSocketService;

    private AuthRepository mAuthRepository;

    private RoomRepository mRoomRepository;

    private ThingRepository mThingRepository;

    private Gson mGson;

    private NotificationHandler mNotificationHandler;

    public static final String INTENT_ERROR_ACTION = "ERROR_MESSAGE";

    public static final String ERROR_KEY = "ERROR";

    private final String TAG = this.getClass().getName();

    public MessageReceiverPresenter(MessageReceiverServiceContract serviceContract,
                                    LiveData<Boolean> wiFiReceiver, WebSocketService webSocketService,
                                    AuthRepository authRepository, RoomRepository roomRepository,
                                    ThingRepository thingRepository, Gson gson, NotificationHandler notificationHandler) {
        mMessageReceiverServiceContract = serviceContract;

        mWiFiReceiver = wiFiReceiver;

        mWebSocketService = webSocketService;

        mAuthRepository = authRepository;

        mRoomRepository = roomRepository;

        mThingRepository = thingRepository;

        mGson = gson;

        mNotificationHandler = notificationHandler;
    }

    public LiveData<Boolean> getWiFiReceiver() {
        return mWiFiReceiver;
    }

    public void connectToServer() {
        mWebSocketService.connect();
    }

    public void sendAuthMessage() {
        String token = mAuthRepository.getToken();

        AuthBody authBody = new AuthBody(token);

        JsonElement jsonBody = mGson.toJsonTree(authBody);

        WebSocketAuthMessage webSocketAuthInfo = new WebSocketAuthMessage(jsonBody);

        String message = mGson.toJson(webSocketAuthInfo, WebSocketAuthMessage.class);

        mWebSocketService.sendMessage(message);
    }

    public void handleResponseMessage(WebSocketMessage message) {
        String topic = message.getTopic();

        if (topic.startsWith("things/")) {
            Thing newThing = mGson.fromJson(message.getBody(), Thing.class);

            updateThing(newThing);

            Room room = findPlacementById(newThing.getPlacement());

            mNotificationHandler.sendEventNotification("Thing in " + newThing.getPlacement() + " has changed",
                    room);
        } else if (topic.startsWith("placements/")) {
            Room newRoom = mGson.fromJson(message.getBody(), Room.class);

            updateRoom(newRoom);
        } else if (topic.startsWith("error")) {
            ApiError apiError = mGson.fromJson(message.getBody(), ApiError.class);

            Log.d(TAG, "Error received. Message : " + apiError.getDevelMessage());

            mMessageReceiverServiceContract.sendErrorBroadcast(apiError);
        }
    }

    public void subscribeOnAllTopics() {
        SubscribeBody subscribeBody = new SubscribeBody("things/#", true);

        JsonElement jsonBody = mGson.toJsonTree(subscribeBody);

        WebSocketMessage webSocketMessage = new WebSocketSubscribeMessage(jsonBody);

        String message = mGson.toJson(webSocketMessage);

        mWebSocketService.sendMessage(message);
    }

    public void updateThing(Thing thing) {
        Disposable disposable = mThingRepository.updateThing(thing)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();

        mCompositeDisposable.add(disposable);
    }

    public void updateRoom(Room room) {
        Disposable disposable = mRoomRepository.updateRoom(room)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();

        mCompositeDisposable.add(disposable);
    }

    public void destroyToken() {
        mAuthRepository.deleteToken();
    }

    public void destroyApiService() {
        mAuthRepository.destroyApiService();
    }

    public void disconnectFromServer() {
        mWebSocketService.disconnect();
    }

    public void sendAcknowledgeMessage(Integer messageId) {
        AcknowledgeBody acknowledgeBody = new AcknowledgeBody(messageId);

        JsonElement jsonBody = mGson.toJsonTree(acknowledgeBody);

        WebSocketAcknowledgeMessage acknowledgeMessage = new WebSocketAcknowledgeMessage(jsonBody);

        String message = mGson.toJson(acknowledgeMessage);

        mWebSocketService.sendMessage(message);
    }

    public Room findPlacementById(String id) {
        return mRoomRepository.getRoomById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .blockingGet();
    }
}
