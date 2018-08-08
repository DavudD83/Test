package space.dotcat.assistant.repository.roomsRepository;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import space.dotcat.assistant.content.Room;

public interface RoomRepository {

    /**
     *
     *  Method is responsible for giving observable flowable of rooms. Firstly trying to load data
     *  from db, if there is no available rooms localy then fetching data via remote api. If some
     *  error occurs, then this method will return merged Flowable from local rooms and delayed error
     *
     * @return observable Flowable list of rooms
     */

    Flowable<List<Room>> getRooms();

    /**
     * Refresh local data source asynchronously via remote api
     *
     * @return flowable which contains list of rooms
     */

    Flowable<List<Room>> refreshRooms();

    /**
     * Update existing room in db
     *
     * @param room object that you want to update
     * @return completable wrapper for doing work on particular scheduler
     */
    Completable updateRoom(Room room);

    /**
     * Get room from database by room id
     *
     * @param id room's id that you want to find
     * @return room wrapped in Maybe
     */

    Maybe<Room> getRoomById(String id);
}
