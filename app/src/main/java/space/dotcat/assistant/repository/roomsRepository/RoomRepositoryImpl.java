package space.dotcat.assistant.repository.roomsRepository;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.RoomResponse;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSource;
import space.dotcat.assistant.utils.RxUtils;

public class RoomRepositoryImpl implements RoomRepository {

    private LocalRoomsSource mLocalRoomsSource;

    private RemoteRoomsSource mRemoteRoomsSource;

    private Flowable<List<Room>> mCacheRooms;
    
    public RoomRepositoryImpl(LocalRoomsSource localRoomsSource, RemoteRoomsSource remoteRoomsSource) {
        mLocalRoomsSource = localRoomsSource;

        mRemoteRoomsSource = remoteRoomsSource;
    }

    @Override
    public Flowable<List<Room>> getRooms() {
        if(mCacheRooms != null)
            return mCacheRooms;

        return mCacheRooms = mLocalRoomsSource.getRooms()
                .flatMap(rooms-> {
                    if(rooms.isEmpty()) {
                        return loadRoomsRemotelyAndSaveToDb().onErrorResumeNext(throwable -> {
                            return Flowable.mergeDelayError(mLocalRoomsSource.getRooms(),
                                    Flowable.error(throwable));
                        });
                    }

                    return Flowable.just(rooms);
                });
    }

    @Override
    public Flowable<List<Room>> refreshRooms() {
        return loadRoomsRemotelyAndSaveToDb();
    }

    @Override
    public Completable updateRoom(Room room) {
        return Completable.fromAction(()-> {
            mLocalRoomsSource.updateRoom(room);
        });
    }

    @Override
    public Maybe<Room> getRoomById(String id) {
        return mLocalRoomsSource.getRoomById(id);
    }

    /**
     * Trying to load rooms from remote server. If successfully loaded then it add sync all rooms from response
     * to database.
     *
     * @return list of room from remote data source
     */

    private Flowable<List<Room>> loadRoomsRemotelyAndSaveToDb() {
        return mRemoteRoomsSource.getRooms()
                .doOnNext(rooms-> {
                    if(rooms.isEmpty()) {
                        return;
                    }

                    mLocalRoomsSource.deleteRoomsSync();

                    mLocalRoomsSource.addRoomsSync(rooms);
                });
    }
}
