package space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import space.dotcat.assistant.content.Room;

@Dao
public interface RoomsDao {

    /**
     *
     * @return flowable which contains list of rooms from database
     */

    @Query("Select * from Rooms")
    Flowable<List<Room>> getRooms();

    /**
     * Notice that this method inserts rooms in a sync way. Take care about it.
     *
     * @param rooms that you want to insert into your database.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRoomsSync(List<Room> rooms);

    /**
     * Delete all rooms from database. Notice that this method deletes rooms in a sync way.
     */

    @Query("Delete from Rooms")
    void deleteRoomsSync();


    /**
     * Update room in db, if it does not exist,it will be inserted into db
     *
     * @param room - object that you want to update
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateRoom(Room room);

    /**
     * Get room from db for room's id
     *
     * @param id room's id
     * @return room wrapped in Maybe
     */

    @Query("Select * from Rooms where room_id = :id")
    Maybe<Room> getRoomById(String id);
}
