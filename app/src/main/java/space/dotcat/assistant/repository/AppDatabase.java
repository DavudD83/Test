package space.dotcat.assistant.repository;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import space.dotcat.assistant.content.BinarySensor;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.ContactSensor;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.PausablePlayer;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Speaker;
import space.dotcat.assistant.content.StringsConverter;
import space.dotcat.assistant.content.TemperatureSensor;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.TrackPlayer;
import space.dotcat.assistant.content.ValueSensor;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.RoomsDao;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.ThingsDao;

@Database(entities = {Room.class,  Lamp.class, DoorLock.class,
        Speaker.class, RGBLamp.class, DimmableLamp.class, ColorTemperatureLamp.class,
        ValueSensor.class, BinarySensor.class, TemperatureSensor.class, ContactSensor.class, Player.class,
        PausablePlayer.class, TrackPlayer.class, Thing.class}, version = 7, exportSchema = false)
@TypeConverters(StringsConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RoomsDao roomsDao();

    public abstract ThingsDao thingsDao();
}
