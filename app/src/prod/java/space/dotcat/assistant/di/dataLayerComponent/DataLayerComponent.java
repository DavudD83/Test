package space.dotcat.assistant.di.dataLayerComponent;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.authActivity.AuthActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.authActivity.AuthActivityModule;
import space.dotcat.assistant.di.activitiesComponents.preferenceFragment.PreferenceFragmentComponent;
import space.dotcat.assistant.di.activitiesComponents.preferenceFragment.PreferenceModule;
import space.dotcat.assistant.di.activitiesComponents.roomDetails.RoomDetailsModule;
import space.dotcat.assistant.di.activitiesComponents.roomDetails.RoomDetailsActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.roomsActivity.RoomListActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.roomsActivity.RoomsModule;
import space.dotcat.assistant.di.activitiesComponents.setupActivity.SetupActivityComponent;
import space.dotcat.assistant.di.activitiesComponents.setupActivity.SetupActivityModule;
import space.dotcat.assistant.di.serviceComponent.MessageReceiverComponent;
import space.dotcat.assistant.di.serviceComponent.WebSocketServiceModule;
import space.dotcat.assistant.di.serviceComponent.WiFiListenerModule;

@Subcomponent(modules = {NetworkModule.class, RepoModule.class})
@DataScope
public interface DataLayerComponent {

    SetupActivityComponent plusSetupComponent(SetupActivityModule setupActivityModule);

    AuthActivityComponent plusAuthComponent(AuthActivityModule authActivityModule);

    RoomListActivityComponent plusRoomsComponent(RoomsModule roomsModule);

    RoomDetailsActivityComponent plusRoomDetailsComponent(RoomDetailsModule roomDetailsModule);

    PreferenceFragmentComponent plusPreferencesComponent(PreferenceModule preferenceModule);

    MessageReceiverComponent plusMessageReceiverComponent(WiFiListenerModule wiFiListenerModule,
                                                          WebSocketServiceModule webSocketServiceModule);
}
