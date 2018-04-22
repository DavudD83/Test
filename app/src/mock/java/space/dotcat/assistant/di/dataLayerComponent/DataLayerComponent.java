package space.dotcat.assistant.di.dataLayerComponent;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.ActivityComponent;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.authRepository.remoteDataSource.RemoteAuthSource;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSource;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.LocalThingsSource;
import space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource.RemoteThingsSource;

@Subcomponent(modules = {NetworkModule.class, RepoModule.class})
@DataScope
public interface DataLayerComponent {

    ActivityComponent plusActivityComponent();

    LocalAuthSource getFakeLocalAuthSource();

    RemoteAuthSource getFakeRemoteAuthSource();

    LocalRoomsSource getFakeLocalRoomsSource();

    RemoteRoomsSource getFakeRemoteRoomsSource();

    LocalThingsSource getFakeLocalThingsSource();

    RemoteThingsSource getFakeRemoteThingsSource();
}
