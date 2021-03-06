package space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;

public interface LocalRoomsSource {

    /**
     * Return observable list of rooms from the db
     *
     * @return flowable with rooms
     */

    Flowable<List<Room>> getRooms();

    /**
     * Add rooms to your db
     *
     * @param rooms that you want to add into your database
     */

    void addRoomsSync(List<Room> rooms);

    /**
     * Delete all rooms from db
     */

    void deleteRoomsSync();

    /**
     * Update particular room
     *
     * @param room object that you want to update
     */

    void updateRoom(Room room);

    /**
     * Get room from database by room's id. Take care that if there is no room with this id in the database then onComplete
     * will be triggered on this stream. This happens due to Room's RxJava support.
     *
     * @param id room's id
     * @return Maybe wrapper of Room.
     */

    Maybe<Room> getRoomById(String id);
}
