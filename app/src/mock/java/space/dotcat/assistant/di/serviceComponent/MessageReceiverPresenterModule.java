package space.dotcat.assistant.di.serviceComponent;

import android.arch.lifecycle.LiveData;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.notifications.NotificationHandler;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.service.MessageReceiverPresenter;
import space.dotcat.assistant.service.MessageReceiverServiceContract;
import space.dotcat.assistant.webSocket.WebSocketService;

@Module
public class MessageReceiverPresenterModule {

    private MessageReceiverServiceContract mMessageReceiverServiceContract;

    public MessageReceiverPresenterModule(MessageReceiverServiceContract messageReceiverServiceContract) {
        mMessageReceiverServiceContract = messageReceiverServiceContract;
    }

    @Provides
    @ServiceScope
    MessageReceiverPresenter provideMessageReceiverPresenter(LiveData<Boolean> wifiListener, WebSocketService webSocketService,
                                                             AuthRepository authRepository, RoomRepository roomRepository,
                                                             ThingRepository thingRepository, Gson gsonConverter,
                                                             NotificationHandler notificationHandler) {
        return new MessageReceiverPresenter(mMessageReceiverServiceContract, wifiListener, webSocketService, authRepository, roomRepository,
                thingRepository, gsonConverter, notificationHandler);
    }

}
