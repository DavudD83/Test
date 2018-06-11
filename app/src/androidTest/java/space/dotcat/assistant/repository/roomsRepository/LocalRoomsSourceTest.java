package space.dotcat.assistant.repository.roomsRepository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.EmptyResultSetException;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class LocalRoomsSourceTest {

    @Rule
    public InstantTaskExecutorRule mTaskExecutorRule = new InstantTaskExecutorRule();

    private LocalRoomsSource mLocalRoomsSource;

    private final List<Room> ROOMS = createRoomsList();

    @Before
    public void init() {
        mLocalRoomsSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeLocalRoomsSource();
    }

    @After
    public void clear() {
        mLocalRoomsSource.deleteRoomsSync();

        mLocalRoomsSource = null;
    }

    @Test
    public void testGetRoomsWhenThereIsNoRoomsInDb() {
        mLocalRoomsSource.getRooms()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertNoValues();
    }

    @Test
    public void testSaveAndGetRooms() {
        mLocalRoomsSource.addRoomsSync(ROOMS);

        mLocalRoomsSource.getRooms()
                .test()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValue(list-> list.get(0).getId().equals(ROOMS.get(0).getId()));
    }

    @Test
    public void testReplaceRoomsDuringInsert() {
        mLocalRoomsSource.addRoomsSync(ROOMS);

        Room room = new Room("corridor",
                "id1", "path");

        mLocalRoomsSource.addRoomsSync(Collections.singletonList(room));

        mLocalRoomsSource.getRooms()
                .flatMap(Flowable::fromIterable)
                .filter(room1-> room1.getId().equals("id1"))
                .test()
                .assertValue(roomInstance-> roomInstance.getFriendlyName().equals(room.getFriendlyName()));
    }

    @Test
    public void testDeleteAllRooms() {
        mLocalRoomsSource.addRoomsSync(ROOMS);

        mLocalRoomsSource.getRooms()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2);

        mLocalRoomsSource.deleteRoomsSync();

        mLocalRoomsSource.getRooms()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0);
    }

    @Test
    public void testGetRoomById() {
        mLocalRoomsSource.addRoomsSync(ROOMS);

        mLocalRoomsSource.getRoomById(ROOMS.get(0).getId())
                .test()
                .assertValue(room -> room.getId().equals(ROOMS.get(0).getId()) &&
                        room.getFriendlyName().equals(ROOMS.get(0).getFriendlyName()))
                .assertNoErrors();

        Room room = mLocalRoomsSource.getRoomById(ROOMS.get(0).getId())
                .blockingGet();

        assertNotNull(room);

        assertEquals(ROOMS.get(0).getFriendlyName(), room.getFriendlyName());
    }

    @Test
    public void testGetRoomByIdWhereIsNoSuchRoomInDb() {
        mLocalRoomsSource.getRoomById(ROOMS.get(0).getId())
                .test()
                .assertComplete();

        Room room = mLocalRoomsSource.getRoomById(ROOMS.get(0).getId())
                .blockingGet();

        assertEquals(null, room);
    }

    private List<Room> createRoomsList() {
        Room room1 = new Room();

        room1.setId("id1");
        room1.setFriendlyName("Corridor");

        Room room2 = new Room();

        room2.setId("id2");
        room2.setFriendlyName("Bathroom");

        return Arrays.asList(room1, room2);
    }
}
