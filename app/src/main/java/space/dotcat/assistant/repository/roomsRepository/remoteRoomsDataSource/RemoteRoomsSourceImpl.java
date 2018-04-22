package space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.api.ApiService;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.RoomResponse;

public class RemoteRoomsSourceImpl implements RemoteRoomsSource {

    private ApiFactory mApiFactory;

    public RemoteRoomsSourceImpl(ApiFactory apiFactory) {
        mApiFactory = apiFactory;
    }

    @Override
    public Flowable<List<Room>> getRooms() {
        return mApiFactory.getApiService()
                .rooms()
                .toFlowable()
                .map(RoomResponse::getRooms);
    }
}
