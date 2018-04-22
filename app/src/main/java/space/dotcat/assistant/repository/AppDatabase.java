package space.dotcat.assistant.repository;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.RoomsDao;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.ThingsDao;

@Database(entities = {Room.class, Thing.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RoomsDao roomsDao();

    public abstract ThingsDao thingsDao();
}
