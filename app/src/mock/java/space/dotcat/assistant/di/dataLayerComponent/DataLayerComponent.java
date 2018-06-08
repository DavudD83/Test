package space.dotcat.assistant.di.dataLayerComponent;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.authActivity.AuthActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.authActivity.AuthActivityModule;
import space.dotcat.assistant.di.activitiesComponents.preferenceFragment.PreferenceFragmentComponent;
import space.dotcat.assistant.di.activitiesComponents.preferenceFragment.PreferenceModule;
import space.dotcat.assistant.di.activitiesComponents.roomDetails.RoomDetailsActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.roomDetails.RoomDetailsModule;
import space.dotcat.assistant.di.activitiesComponents.roomsActivity.RoomListActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.roomsActivity.RoomsModule;
import space.dotcat.assistant.di.activitiesComponents.setupActivity.SetupActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.setupActivity.SetupActivityModule;
import space.dotcat.assistant.di.serviceComponent.MessageReceiverComponent;
import space.dotcat.assistant.di.serviceComponent.MessageReceiverPresenterModule;
import space.dotcat.assistant.di.serviceComponent.WebSocketServiceModule;
import space.dotcat.assistant.di.serviceComponent.WiFiListenerModule;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.authRepository.remoteDataSource.RemoteAuthSource;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSource;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.LocalThingsSource;
import space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource.RemoteThingsSource;

@Subcomponent(modules = {NetworkModule.class, RepoModule.class, ServiceHandlerModule.class})
@DataScope
public interface DataLayerComponent {

    AuthActivityComponent plusAuthComponent(AuthActivityModule authActivityModule);

    RoomListActivityComponent plusRoomsComponent(RoomsModule roomsModule);

    RoomDetailsActivityComponent plusRoomDetailsComponent(RoomDetailsModule roomDetailsModule);

    PreferenceFragmentComponent plusPreferencesComponent(PreferenceModule preferenceModule);

    SetupActivityComponent plusSetupComponent(SetupActivityModule setupActivityModule);

    MessageReceiverComponent plusMessageReceiverComponent(WiFiListenerModule wiFiListenerModule,
                                                          WebSocketServiceModule webSocketServiceModule,
                                                          MessageReceiverPresenterModule receiverPresenterModule);

    LocalAuthSource getFakeLocalAuthSource();

    RemoteAuthSource getFakeRemoteAuthSource();

    LocalRoomsSource getFakeLocalRoomsSource();

    RemoteRoomsSource getFakeRemoteRoomsSource();

    LocalThingsSource getFakeLocalThingsSource();

    RemoteThingsSource getFakeRemoteThingsSource();
}
