package space.dotcat.assistant.service;

import android.arch.lifecycle.LifecycleService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.content.webSocketModel.WebSocketMessage;
import space.dotcat.assistant.di.serviceComponent.MessageReceiverPresenterModule;
import space.dotcat.assistant.di.serviceComponent.WebSocketServiceModule;
import space.dotcat.assistant.di.serviceComponent.WiFiListenerModule;
import space.dotcat.assistant.screen.general.BaseActivity;
import space.dotcat.assistant.webSocket.WebSocketServiceImpl;

/**
 * Service that is responsible for listening to new updates and dispatching corresponding notifications
 */

public class MessageReceiverService extends LifecycleService implements MessageReceiverServiceContract {

    @Inject
    MessageReceiverPresenter mMessageReceiverPresenter;

    private final String TAG = this.getClass().getName();

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

                mMessageReceiverPresenter.subscribeOnAllTopics();
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
                        new MessageReceiverPresenterModule(this))
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

    @Override
    public void sendErrorBroadcast(ApiError error) {
        Intent errorIntent = new Intent(MessageReceiverPresenter.INTENT_ERROR_ACTION);
        errorIntent.putExtra(MessageReceiverPresenter.ERROR_KEY, error);

        sendBroadcast(errorIntent);
    }

    private class MessageServiceListener implements WebSocketServiceImpl.ServerListener {

        @Override
        public void onMessage(WebSocketMessage message) {
            if (message.getMessageId() != null) {
                mMessageReceiverPresenter.sendAcknowledgeMessage(message.getMessageId());

                Log.d(TAG, "Message id received " + message.getMessageId());
            }

            mMessageReceiverPresenter.handleResponseMessage(message);

            Log.d(TAG, "Message received. Topic is " + message.getTopic());
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

            mMessageReceiverPresenter.subscribeOnAllTopics();

            Log.d(TAG, "error received " + throwable.getLocalizedMessage());
        }
    }
}
