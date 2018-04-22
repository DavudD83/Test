package space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.RoomResponse;

public interface RemoteRoomsSource {
    /**
     * Return list of rooms from remote api
     *
     * @return list of rooms
     */
    Flowable<List<Room>> getRooms();
}
