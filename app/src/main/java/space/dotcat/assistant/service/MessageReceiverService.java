package space.dotcat.assistant.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.WebSocketMessage;
import space.dotcat.assistant.di.serviceComponent.WebSocketServiceModule;
import space.dotcat.assistant.di.serviceComponent.WiFiListenerModule;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.webSocket.WebSocketService;
import space.dotcat.assistant.webSocket.WebSocketServiceImpl;

/**
 * Service that is responsible for listening to new updates and dispatching corresponding notifications
 */

public class MessageReceiverService extends LifecycleService {

    @Inject
    LiveData<Boolean> mWiFiListener;

    @Inject
    WebSocketService mWebSocketService;

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    RoomRepository mRoomRepository;

    @Inject
    ThingRepository mThingRepository;

    @Inject
    Gson mGson;

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

        initDependencyGraph();

        mWiFiListener.observe(this, isConnected -> {
            if (isConnected) {
                mWebSocketService.connect();
            }
        });
    }

    private void initDependencyGraph() {
        AppDelegate.getInstance()
                .plusDataLayerComponent()
                .plusMessageReceiverComponent(new WiFiListenerModule(),
                        new WebSocketServiceModule(new MessageServiceListener()))
                .inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mAuthRepository.saveMessageServiceState(true);

        return START_STICKY; //when return start_sticky it means that service will be re-created if it stops
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mWebSocketService.disconnect();

        mAuthRepository.saveMessageServiceState(false);
    }

    private class MessageServiceListener implements WebSocketServiceImpl.ServerListener {

        @Override
        public void onMessage(WebSocketMessage message) {
            String topic = message.getTopic();

            if (topic.startsWith("things/")) {
                Thing newThing = mGson.fromJson(message.getBody(), Thing.class);

                mThingRepository.updateThing(newThing)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe();

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "updates")
                        .setContentTitle("Update notification")
                        .setContentText("Thing in" + newThing.getPlacement() + " has changed")
                        .setSmallIcon(R.drawable.leak_canary_icon)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(0, notification);

            } else if (topic.startsWith("placements/")) {
                Room newRoom = mGson.fromJson(message.getBody(), Room.class);

                //TODO
            }



            Log.d("MessageReceiverService", "message received " + message.getTimestamp());
        }

        @Override
        public void onError(Throwable throwable) {
            Log.d("MessageReceiverService", "error received " + throwable.getMessage());
        }
    }
}
