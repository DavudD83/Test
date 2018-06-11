package space.dotcat.assistant.repository.thingsRepository.localThingsDataSource;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function4;
import space.dotcat.assistant.content.BinarySensor;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.ContactSensor;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.PausablePlayer;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Speaker;
import space.dotcat.assistant.content.TemperatureSensor;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.TrackPlayer;
import space.dotcat.assistant.content.ValueSensor;
import space.dotcat.assistant.utils.Func;

@Dao
public abstract class ThingsDao {

    private static final HashMap<Class<?>, Func> mInsertFunctions = new LinkedHashMap<>();

    private static final HashMap<Class<?>, Func> mUpdateFunctions = new LinkedHashMap<>();

    public ThingsDao() {
        mInsertFunctions.put(DoorLock.class, (Func<DoorLock>) this::insertThingByType);
        mInsertFunctions.put(Lamp.class, (Func<Lamp>) this::insertThingByType);
        mInsertFunctions.put(Speaker.class, (Func<Speaker>) this::insertThingByType);
        mInsertFunctions.put(RGBLamp.class, (Func<RGBLamp>) this::insertThingByType);
        mInsertFunctions.put(DimmableLamp.class, (Func<DimmableLamp>) this::insertThingByType);
        mInsertFunctions.put(ColorTemperatureLamp.class, (Func<ColorTemperatureLamp>) this::insertThingByType);
        mInsertFunctions.put(ValueSensor.class, (Func<ValueSensor>) this::insertThingByType);
        mInsertFunctions.put(BinarySensor.class, (Func<BinarySensor>) this::insertThingByType);
        mInsertFunctions.put(TemperatureSensor.class, (Func<TemperatureSensor>) this::insertThingByType);
        mInsertFunctions.put(ContactSensor.class, (Func<ContactSensor>) this::insertThingByType);
        mInsertFunctions.put(Player.class, (Func<Player>) this::insertThingByType);
        mInsertFunctions.put(PausablePlayer.class, (Func<PausablePlayer>) this::insertThingByType);
        mInsertFunctions.put(TrackPlayer.class, (Func<TrackPlayer>) this::insertThingByType);

        mUpdateFunctions.put(DoorLock.class, (Func<DoorLock>) this::updateThingByType);
        mUpdateFunctions.put(Lamp.class, (Func<Lamp>) this::updateThingByType);
        mUpdateFunctions.put(Speaker.class, (Func<Speaker>) this::updateThingByType);
        mUpdateFunctions.put(RGBLamp.class, (Func<RGBLamp>) this::updateThingByType);
        mUpdateFunctions.put(DimmableLamp.class, (Func<DimmableLamp>) this::updateThingByType);
        mUpdateFunctions.put(ColorTemperatureLamp.class, (Func<ColorTemperatureLamp>) this::updateThingByType);
        mUpdateFunctions.put(ValueSensor.class, (Func<ValueSensor>) this::updateThingByType);
        mUpdateFunctions.put(BinarySensor.class, (Func<BinarySensor>) this::updateThingByType);
        mUpdateFunctions.put(TemperatureSensor.class, (Func<TemperatureSensor>) this::updateThingByType);
        mUpdateFunctions.put(ContactSensor.class, (Func<ContactSensor>) this::updateThingByType);
        mUpdateFunctions.put(Player.class, (Func<Player>) this::updateThingByType);
        mUpdateFunctions.put(PausablePlayer.class, (Func<PausablePlayer>) this::updateThingByType);
        mUpdateFunctions.put(TrackPlayer.class, (Func<TrackPlayer>) this::updateThingByType);
    }

    /**
     * Get things by particular placement id from local database.
     * This is observable query, so each time when database will be updated,
     * flowable's onNext will be called
     *
     * @param id placement id
     * @return flowable which contains list of things from database.
     */

    @Query("Select * from Door_Locks, Lamps, Speakers, Dimmable_Lamps, RGB_Lamps where" +
            " Door_Locks.thing_placement = :id and Lamps.thing_placement = :id and Speakers.thing_placement = :id and Dimmable_Lamps.thing_placement = :id and RGB_Lamps.thing_placement = :id")
    public Flowable<List<Thing>> getAllThings(String id) {
        return Flowable.combineLatest(
                getDoorLocks(id),
                getLamps(id),
                getDimmableLamps(id),
                getColorTemperatureLamps(id),
                getRGBLamps(id),
                (doorLocks, lamps, dimmableLamps, colorTemperatureLamps, rgbLamps) -> {
                    List<Thing> things = new ArrayList<>();

                    things.addAll(doorLocks);
                    things.addAll(lamps);
                    things.addAll(dimmableLamps);
                    things.addAll(colorTemperatureLamps);
                    things.addAll(rgbLamps);

                    return things;
                }

        );
    }

    @Query("Select * from Door_Locks, Lamps, Speakers where Door_Locks.thing_placement = :id and " +
            "Lamps.thing_placement = :id and Speakers.thing_placement = :id")
    public Flowable<List<Thing>> getAllThingsById(String id) {
        Flowable<List<Thing>> firstThings = Flowable.combineLatest(
                getDefaultThings(id),
                getDoorLocks(id),
                getLamps(id),
                getSpeakers(id),
                getRGBLamps(id),
                getDimmableLamps(id),
                getColorTemperatureLamps(id),
                getValueSensors(id),
                getBinarySensors(id),
                (defaultThings, doorLocks, lamps, speakers, rgbLamps, dimmableLamps, colorTemperatureLamps, valueSensors,
                 binarySensors) -> {
                    List<Thing> things = new ArrayList<>();

                    things.addAll(defaultThings);
                    things.addAll(doorLocks);
                    things.addAll(lamps);
                    things.addAll(speakers);
                    things.addAll(rgbLamps);
                    things.addAll(dimmableLamps);
                    things.addAll(colorTemperatureLamps);
                    things.addAll(valueSensors);
                    things.addAll(binarySensors);

                    return things;
                }
        );

        Flowable<List<Thing>> secondThings = Flowable.combineLatest(
                getTemperatureSensors(id),
                getContactSensors(id),
                getPlayers(id),
                getPausablePlayers(id),
                getTrackPlayers(id),
                (temperatureSensors, contactSensors, players, pausablePlayers, trackPlayers) -> {
                    List<Thing> things = new ArrayList<>();

                    things.addAll(temperatureSensors);
                    things.addAll(contactSensors);
                    things.addAll(players);
                    things.addAll(pausablePlayers);
                    things.addAll(trackPlayers);

                    return things;
                });

        return Flowable.combineLatest(firstThings, secondThings, (things, things2) -> {
            List<Thing> thingList = new ArrayList<>();

            thingList.addAll(things);
            thingList.addAll(things2);

            return thingList;
        });
    }
//
    @Query("Select * from Things where thing_placement = :id")
    public abstract Flowable<List<Thing>> getDefaultThings(String id);

    @Query("Select * from Door_Locks where Door_Locks.thing_placement = :id")
    public abstract Flowable<List<DoorLock>> getDoorLocks(String id);

    @Query("Select * from Lamps where Lamps.thing_placement = :id")
    public abstract Flowable<List<Lamp>> getLamps(String id);

    @Query("Select * from Speakers where Speakers.thing_placement = :id")
    public abstract Flowable<List<Speaker>> getSpeakers(String id);

    @Query("Select * from RGB_Lamps where RGB_Lamps.thing_placement = :id")
    public abstract Flowable<List<RGBLamp>> getRGBLamps(String id);

    @Query("Select * from Dimmable_Lamps where Dimmable_Lamps.thing_placement = :id")
    public abstract Flowable<List<DimmableLamp>> getDimmableLamps(String id);

    @Query("Select * from Color_Temperature_Lamps where thing_placement = :id")
    public abstract Flowable<List<ColorTemperatureLamp>> getColorTemperatureLamps(String id);

    @Query("Select * from ValueSensors where thing_placement = :id")
    public abstract Flowable<List<ValueSensor>> getValueSensors(String id);

    @Query("Select * from Binary_Sensors where thing_placement = :id")
    public abstract Flowable<List<BinarySensor>> getBinarySensors(String id);

    @Query("Select * from Temperature_Sensors where thing_placement = :id")
    public abstract Flowable<List<TemperatureSensor>> getTemperatureSensors(String id);

    @Query("Select * from Contact_Sensors where thing_placement = :id")
    public abstract Flowable<List<ContactSensor>> getContactSensors(String id);

    @Query("Select * from Players where thing_placement = :id")
    public abstract Flowable<List<Player>> getPlayers(String id);

    @Query("Select * from Pausable_Players where thing_placement = :id")
    public abstract Flowable<List<PausablePlayer>> getPausablePlayers(String id);

    @Query("Select * from Track_Players where thing_placement = :id")
    public abstract Flowable<List<TrackPlayer>> getTrackPlayers(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDefaultThing(Thing thing);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(DoorLock doorLock);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(Speaker speaker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(Lamp lamp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(RGBLamp rgbLamp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(DimmableLamp dimmableLamp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(ColorTemperatureLamp colorTemperatureLamp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(Player player);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(PausablePlayer player);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(TrackPlayer trackPlayer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(ValueSensor valueSensor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(BinarySensor binarySensor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(TemperatureSensor tempertatureSensor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertThingByType(ContactSensor contactSensor);


    /**
     * Insert things into database. If some conflict occurs, thing will be completely replaced
     *
     * @param things list of things that you want to insert into database.
     */

    @Transaction
    public void insertThings(List<Thing> things) {
        for (Thing item : things) {
//            if (item instanceof DoorLock) {
//                DoorLock doorLock = (DoorLock) item;
//
//                insertThingByType(doorLock);
//            } else if (item instanceof Lamp) {
//                Lamp lamp = (Lamp) item;
//
//                insertLamp(lamp);
//            } else if (item instanceof Speaker) {
//                Speaker speaker = (Speaker) item;
//
//                insertThingByType(speaker);
//            }
//        }
            if (mInsertFunctions.containsKey(item.getClass())) {
                mInsertFunctions.get(item.getClass()).invoke(item);
            } else {
                insertDefaultThing(item);
            }
        }
    }

    @Transaction
    public void insertLamp(Lamp lamp) {
        if (lamp instanceof DimmableLamp) {
            DimmableLamp dimmableLamp = (DimmableLamp) lamp;

            insertThingByType(dimmableLamp);
        } else if (lamp instanceof RGBLamp) {
            RGBLamp rgbLamp = (RGBLamp) lamp;

            insertThingByType(rgbLamp);
        } else if (lamp instanceof Lamp) {
            insertThingByType(lamp);
        }
    }

    @Transaction
    public void insertThing(Thing thing)
    {
        Class<?> thingClass = thing.getClass();

        if (mInsertFunctions.containsKey(thingClass)) {
            mInsertFunctions.get(thingClass).invoke(thing);
        } else {
            insertDefaultThing(thing);
        }

//        if (thing instanceof DoorLock) {
//            DoorLock doorLock = (DoorLock) thing;
//
//            insertThingByType(doorLock);
//        } else if (thing instanceof Lamp) {
//            Lamp lamp = (Lamp) thing;
//
//            insertLamp(lamp);
//        } else if (thing instanceof Speaker) {
//            Speaker speaker = (Speaker) thing;
//
//            insertThingByType(speaker);
//        }
    }

    /**
     * Delete all things from database
     */

    @Transaction
    public void deleteAllThings() {
        deleteAllDefaultThings();
        deleteAllDoorLocks();
        deleteAllLamps();
        deleteAllSpeakers();
        deleteAllRGBLamps();
        deleteAllDimmableLamps();
        deleteAllColorTemperatureLamps();
        deleteAllValueSensors();
        deleteAllBinarySensors();
        deleteAllTemperatureSensors();
        deleteAllContactSensors();
        deleteAllPlayers();
        deleteAllPausablePlayers();
        deleteAllTrackPlayers();
    }

    @Query("Delete from Things")
    abstract void deleteAllDefaultThings();

    @Query("Delete from Door_Locks")
    public abstract void deleteAllDoorLocks();

    @Query("Delete from Lamps")
    public abstract void deleteAllLamps();

    @Query("Delete from Speakers")
    public abstract void deleteAllSpeakers();

    @Query("Delete from RGB_Lamps")
    public abstract void deleteAllRGBLamps();

    @Query("Delete from Dimmable_Lamps")
    public abstract void deleteAllDimmableLamps();

    @Query("Delete from Color_Temperature_Lamps")
    public abstract void deleteAllColorTemperatureLamps();

    @Query("Delete from ValueSensors")
    public abstract void deleteAllValueSensors();

    @Query("Delete from Binary_Sensors")
    public abstract void deleteAllBinarySensors();

    @Query("Delete from Temperature_Sensors")
    public abstract void deleteAllTemperatureSensors();

    @Query("Delete from Players")
    public abstract void deleteAllPlayers();

    @Query("Delete from Pausable_Players")
    public abstract void deleteAllPausablePlayers();

    @Query("Delete from Track_Players")
    public abstract void deleteAllTrackPlayers();

    @Query("Delete from Contact_Sensors")
    public abstract void deleteAllContactSensors();


    /**
     * Delete particular thing by its id
     *
     * @param id thing id ,that you want to delete
     */

    @Query("Delete from Things where thing_id = :id")
    abstract void deleteThingById(String id);


    /**
     * Update your thing
     *
     * @param thing thing that you want to update
     */

    @Transaction
    public void updateThing(Thing thing) {
        Class<?> thingClass = thing.getClass();

        if (mUpdateFunctions.containsKey(thingClass)) {
            mUpdateFunctions.get(thingClass).invoke(thing);
        } else {
            updateDefaultThing(thing);
        }


//        if (thing instanceof DoorLock) {
//            DoorLock doorLock = (DoorLock) thing;
//
//            updateThingByType(doorLock);
//        } else if (thing instanceof Lamp) {
//            Lamp lamp = (Lamp) thing;
//
//            updateLamp(lamp);
//        } else if (thing instanceof Speaker) {
//            Speaker speaker = (Speaker) thing;
//
//            updateThingByType(speaker);
//        }
    }

    @Transaction
    public void updateLamp(Lamp lamp) {
        if (lamp instanceof DimmableLamp) {
            DimmableLamp dimmableLamp = (DimmableLamp) lamp;

            updateThingByType(dimmableLamp);
        } else if (lamp instanceof RGBLamp) {
            RGBLamp rgbLamp = (RGBLamp) lamp;

            updateThingByType(rgbLamp);
        } else if (lamp instanceof Lamp) {
            updateThingByType(lamp);
        }
    }


    @Update
    abstract void updateDefaultThing(Thing thing);

    @Update
    public abstract void updateThingByType(DoorLock doorLock);

    @Update
    public abstract void updateThingByType(Lamp lamp);

    @Update
    public abstract void updateThingByType(Speaker speaker);

    @Update
    public abstract void updateThingByType(RGBLamp rgbLamp);

    @Update
    public abstract void updateThingByType(DimmableLamp dimmableLamp);

    @Update
    public abstract void updateThingByType(ColorTemperatureLamp colorTemperatureLamp);

    @Update
    public  abstract void updateThingByType(Player player);

    @Update
    public abstract void updateThingByType(PausablePlayer pausablePlayer);

    @Update
    public abstract void updateThingByType(TrackPlayer trackPlayer);

    @Update
    public abstract void updateThingByType(ValueSensor valueSensor);

    @Update
    public abstract void updateThingByType(BinarySensor binarySensor);

    @Update
    public abstract void updateThingByType(TemperatureSensor tempertatureSensor);

    @Update
    public abstract void updateThingByType(ContactSensor contactSensor);

    @Transaction
    public void deleteAndInsertThings(List<Thing> things) {
        deleteAllThings();

        insertThings(things);
    }

}
