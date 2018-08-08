package space.dotcat.assistant.repository.roomsRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class RoomRepositoryTest {

    @Mock
    private LocalRoomsSource mLocalAuthSource;

    @Mock
    private RemoteRoomsSource mRemoteAuthSource;

    private RoomRepository mRoomRepository;

    private final List<Room> ROOMS = Arrays.asList(new Room(), new Room(), new Room());

    private final List<Room> EMPTY_ROOMS = new ArrayList<>();

    private final Throwable THROWABLE = new Throwable();

    private final Flowable<List<Room>> FLOWABLE_ROOMS = Flowable.just(ROOMS);

    private final Flowable<List<Room>> FLOWABLE_ROOMS_EMPTY = Flowable.just(EMPTY_ROOMS);

    private final Flowable<List<Room>> FLOWABLE_WITH_ERROR = Flowable.error(THROWABLE);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mRoomRepository = new RoomRepositoryImpl(mLocalAuthSource, mRemoteAuthSource);
    }

    @After
    public void clear() {
        mLocalAuthSource = null;
        mRemoteAuthSource = null;
        mRoomRepository = null;
    }

    @Test
    public void testGetRoomsFromLocalDataSource() {
        when(mLocalAuthSource.getRooms()).thenReturn(FLOWABLE_ROOMS);

        mRoomRepository.getRooms()
                .test()
                .assertValue(ROOMS);

        verify(mLocalAuthSource).getRooms();

        verifyNoMoreInteractions(mRemoteAuthSource);
    }

    @Test
    public void testRefreshRoomsWithError() {
        when(mRemoteAuthSource.getRooms()).thenReturn(FLOWABLE_WITH_ERROR);

        mRoomRepository.refreshRooms()
                .test()
                .assertError(THROWABLE);
    }

    @Test
    public void testRefreshRoomsEmpty() {
        when(mRemoteAuthSource.getRooms()).thenReturn(FLOWABLE_ROOMS_EMPTY);

        mRoomRepository.refreshRooms()
                .test()
                .assertValue(EMPTY_ROOMS);

        verifyNoMoreInteractions(mLocalAuthSource);
    }

    @Test
    public void testRefreshRoomsSuccessfully() {
        when(mRemoteAuthSource.getRooms()).thenReturn(FLOWABLE_ROOMS);

        mRoomRepository.refreshRooms()
                .test()
                .assertValue(ROOMS);

        verify(mLocalAuthSource).deleteRoomsSync();

        verify(mLocalAuthSource).addRoomsSync(ROOMS);
    }
}
