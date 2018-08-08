package space.dotcat.assistant.di.dataLayerComponent;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.repository.authRepository.AuthRepositoryImpl;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSourceImpl;
import space.dotcat.assistant.repository.authRepository.remoteDataSource.RemoteAuthSource;
import space.dotcat.assistant.repository.authRepository.remoteDataSource.RemoteAuthSourceImpl;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.repository.roomsRepository.RoomRepositoryImpl;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSourceImpl;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.RoomsDao;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSource;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSourceImpl;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.repository.thingsRepository.ThingRepositoryImpl;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.LocalThingsSource;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.LocalThingsSourceImpl;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.ThingsDao;
import space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource.RemoteThingsSource;
import space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource.RemoteThingsSourceImpl;

@Module
public class RepoModule {

    @Provides
    @DataScope
    LocalAuthSource provideLocalAuthSource(SharedPreferences sharedPreferences) {
        return new LocalAuthSourceImpl(sharedPreferences);
    }

    @Provides
    @DataScope
    RemoteAuthSource provideRemoteAuthSource(ApiFactory apiFactory) {
        return new RemoteAuthSourceImpl(apiFactory);
    }

    @Provides
    @DataScope
    AuthRepository provideAuthRepo(LocalAuthSource localAuthSource, RemoteAuthSource remoteAuthSource) {
        return new AuthRepositoryImpl(localAuthSource, remoteAuthSource);
    }

    @Provides
    @DataScope
    LocalRoomsSource provideLocalRoomsSource(RoomsDao roomsDao) {
        return new LocalRoomsSourceImpl(roomsDao);
    }

    @Provides
    @DataScope
    RemoteRoomsSource provideRemoteRoomsSource(ApiFactory apiFactory) {
        return new RemoteRoomsSourceImpl(apiFactory);
    }

    @Provides
    @DataScope
    RoomRepository provideRoomRepository(LocalRoomsSource localRoomsSource, RemoteRoomsSource remoteRoomsSource) {
        return new RoomRepositoryImpl(localRoomsSource, remoteRoomsSource);
    }

    @Provides
    @DataScope
    LocalThingsSource provideLocalThingsSource(ThingsDao thingsDao) {
        return new LocalThingsSourceImpl(thingsDao);
    }

    @Provides
    @DataScope
    RemoteThingsSource provideRemoteThingsSource(ApiFactory apiFactory) {
        return new RemoteThingsSourceImpl(apiFactory);
    }

    @Provides
    @DataScope
    ThingRepository provideThingRepository(LocalThingsSource localThingsSource, RemoteThingsSource remoteThingsSource) {
        return new ThingRepositoryImpl(localThingsSource, remoteThingsSource);
    }
}
