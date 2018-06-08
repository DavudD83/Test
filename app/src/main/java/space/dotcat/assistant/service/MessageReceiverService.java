package space.dotcat.assistant.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LifecycleService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Inject;

import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.webSocketModel.WebSocketMessage;
import space.dotcat.assistant.di.serviceComponent.MessageReceiverPresenterModule;
import space.dotcat.assistant.di.serviceComponent.WebSocketServiceModule;
import space.dotcat.assistant.di.serviceComponent.WiFiListenerModule;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsActivity;
import space.dotcat.assistant.webSocket.WebSocketServiceImpl;

/**
 * Service that is responsible for listening to new updates and dispatching corresponding notifications
 */

public class MessageReceiverService extends LifecycleService {

    @Inject
    MessageReceiverPresenter mMessageReceiverPresenter;

    @Inject
    Gson mGson;

    private final String TAG = this.getClass().getName();

    public static final String INTENT_ERROR_ACTION = "ERROR_MESSAGE";

    public static final String ERROR_KEY = "ERROR";

    public static Boolean sIsWorking;

    public static Intent getIntent(Context context) {
        return new Intent(context, MessageReceiverService.class);
    }

    public MessageReceiverService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "OnCreateService");

        initDependencyGraph();

        mMessageReceiverPresenter.getWiFiReceiver().observe(this, isConnected -> {
            if (isConnected) {
                Log.d(TAG, "WiFi is connected");

                mMessageReceiverPresenter.connectToServer();

                mMessageReceiverPresenter.sendAuthMessage();

                mMessageReceiverPresenter.subscribeOnTopics();
            } else {
                Log.d(TAG, "WiFi is disconnected");
            }
        });

        sIsWorking = true;
    }

    private void initDependencyGraph() {
        AppDelegate.getInstance()
                .plusDataLayerComponent()
                .plusMessageReceiverComponent(new WiFiListenerModule(),
                        new WebSocketServiceModule(new MessageServiceListener()),
                        new MessageReceiverPresenterModule())
                .inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "onStartCommandService");

        return START_STICKY; //when return start_sticky it means that service will be re-created if it stops
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroyService");

        mMessageReceiverPresenter.unsubscribe();

        mMessageReceiverPresenter.disconnectFromServer();

        sIsWorking = false;
    }

    private class MessageServiceListener implements WebSocketServiceImpl.ServerListener {

        @Override
        public void onMessage(WebSocketMessage message) {
            if (message.getMessageId() != null) {
                mMessageReceiverPresenter.sendAcknowledgeMessage(message.getMessageId());

                Log.d(TAG, "message id received " + message.getMessageId());
            }

            String topic = message.getTopic();

            if (topic.startsWith("things/")) {
                Thing newThing = mGson.fromJson(message.getBody(), Thing.class);

                mMessageReceiverPresenter.updateThing(newThing);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "updates")
                        .setContentTitle("Update notification")
                        .setContentText("Thing in" + newThing.getPlacement() + " has changed")
                        .setSmallIcon(R.drawable.ic_notification)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setAutoCancel(true);

                final Room placement = mMessageReceiverPresenter.findPlacementById(newThing.getPlacement());

                if (placement != null) {
                    Intent launchIntent = RoomDetailsActivity.getIntent(getApplicationContext(), placement);

                    PendingIntent launchPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                            launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(launchPendingIntent);
                }

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(0, builder.build());
            } else if (topic.startsWith("placements/")) {
                Room newRoom = mGson.fromJson(message.getBody(), Room.class);

                mMessageReceiverPresenter.updateRoom(newRoom);
            } else if (topic.startsWith("error")) {
                ApiError apiError = mGson.fromJson(message.getBody(), ApiError.class);

                Log.d(TAG, apiError.getDevelMessage());

                Intent errorBroadcast = new Intent(INTENT_ERROR_ACTION);
                errorBroadcast.putExtra(ERROR_KEY, apiError);

                sendBroadcast(errorBroadcast);
            }

            Log.d("MessageReceiverService", "message received " + message.getTopic());
        }

        @Override
        public void onError(Throwable throwable) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mMessageReceiverPresenter.connectToServer();

            mMessageReceiverPresenter.sendAuthMessage();

            mMessageReceiverPresenter.subscribeOnTopics();

            Log.d(TAG, "error received " + throwable.getLocalizedMessage());
        }
    }
}
