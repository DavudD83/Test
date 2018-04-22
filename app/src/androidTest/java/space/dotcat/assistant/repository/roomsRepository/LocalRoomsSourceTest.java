package space.dotcat.assistant.repository.roomsRepository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
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
import space.dotcat.assistant.repository.roomsRepository.localRoomsDataSource.LocalRoomsSource;

@RunWith(AndroidJUnit4.class)
public class LocalRoomsSourceTest {

    @Rule
    public InstantTaskExecutorRule mTaskExecutorRule = new InstantTaskExecutorRule();

    private LocalRoomsSource mLocalRoomsSource;

    private final List<space.dotcat.assistant.content.Room> ROOMS = createRoomsList();

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
                .assertValue(list-> list.get(0).getId().equals("id1"));
    }

    @Test
    public void testReplaceRoomsDuringInsert() {
        mLocalRoomsSource.addRoomsSync(ROOMS);

        space.dotcat.assistant.content.Room room = new space.dotcat.assistant.content.Room("corridor",
                "id1", "path");

        mLocalRoomsSource.addRoomsSync(Collections.singletonList(room));

        mLocalRoomsSource.getRooms()
                .flatMap(Flowable::fromIterable)
                .filter(room1-> room1.getId().equals("id1"))
                .test()
                .assertValue(roomInstance-> roomInstance.getFriendlyName().equals("corridor"));
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

    private List<space.dotcat.assistant.content.Room> createRoomsList() {
        space.dotcat.assistant.content.Room room1 = new space.dotcat.assistant.content.Room();

        room1.setId("id1");

        space.dotcat.assistant.content.Room room2 = new space.dotcat.assistant.content.Room();

        room2.setId("id2");

        return Arrays.asList(room1, room2);
    }
}
