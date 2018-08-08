package space.dotcat.assistant.di.appComponent;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.repository.AppDatabase;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSourceImpl;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.RoomsDao;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSource;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.ThingsDao;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    AppDatabase provideRoomDatabase(Context context) {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration() //be careful about this line
                .build();

    }

    @Provides
    @Singleton
    RoomsDao provideRoomsDao(AppDatabase appDatabase) {
        return appDatabase.roomsDao();
    }

    @Provides
    @Singleton
    ThingsDao provideThingsDao(AppDatabase appDatabase) {
        return appDatabase.thingsDao();
    }
}
