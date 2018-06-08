package space.dotcat.assistant.repository.thingsRepository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;

import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.LocalThingsSource;

@RunWith(AndroidJUnit4.class)
public class LocalThingsSourceTest {

    @Rule
    public InstantTaskExecutorRule mTaskExecutorRule = new InstantTaskExecutorRule();

    private LocalThingsSource mLocalThingsSource;

    public final String ROOM_ID = "R1";

    private final List<Thing> THINGS = createThings();

    @Before
    public void init() {
        mLocalThingsSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeLocalThingsSource();
    }

    @After
    public void clear() {
        mLocalThingsSource.deleteAllThings();

        mLocalThingsSource = null;
    }

    @Test
    public void testGetThingsWhenThereIsNoThingsLocaly() {
        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0);
    }

    @Test
    public void testGetThings() {
        mLocalThingsSource.addThingsSync(THINGS);

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(3)
                .assertValueAt(0, thing -> thing.getId().equals("id"))
                .assertValueAt(1, thing -> thing.getId().equals("id1") && !thing.getIsAvailable())
                .assertValueAt(2, thing -> thing.getPlacement().equals(ROOM_ID));
    }

    @Test
    public void testDeleteThings() {
        mLocalThingsSource.addThingsSync(THINGS);

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueAt(0, thing -> thing.getPlacement().equals(ROOM_ID));

        mLocalThingsSource.deleteAllThings();

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0);
    }

    @Test
    public void testUpdateThings() {
        mLocalThingsSource.addThingsSync(THINGS);

        Thing updatedThing = THINGS.get(0);
        updatedThing.setAvailable(true);

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueAt(0, thing -> thing.getPlacement().equals(ROOM_ID) && !thing.getIsAvailable());

        mLocalThingsSource.updateThing(updatedThing);

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueAt(0, Thing::getIsAvailable);
    }

    @Test
    public void testInsertThingsWithReplacing() {
        mLocalThingsSource.addThingsSync(THINGS);

        DoorLock thing = new DoorLock();
        thing.setId("id");
        thing.setPlacement(ROOM_ID);
        thing.setIsActive(true);
        thing.setAvailable(true);

        Lamp thing1 = new Lamp();
        thing1.setId("id1");
        thing1.setPlacement(ROOM_ID);
        thing1.setActive(false);
        thing1.setAvailable(true);

        DimmableLamp thing2 = new DimmableLamp();
        thing2.setId("id2");
        thing2.setPlacement(ROOM_ID);
        thing2.setActive(true);
        thing2.setAvailable(true);

        mLocalThingsSource.addThingsSync(Arrays.asList(thing, thing1, thing2));

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(4)
                .assertValueAt(1, Thing::getIsAvailable)
                .assertValueAt(2, Thing::getIsAvailable);
    }


    private List<Thing> createThings() {
        DoorLock door = new DoorLock();

        door.setPlacement(ROOM_ID);
        door.setId("id");
        door.setIsActive(false);
        door.setAvailable(false);

        Lamp light = new Lamp();

        light.setPlacement(ROOM_ID);
        light.setId("id1");
        light.setActive(false);
        light.setAvailable(false);

        DimmableLamp player = new DimmableLamp();

        player.setPlacement(ROOM_ID);
        player.setId("id2");
        player.setActive(true);

        Thing anotherThing = new Thing();

        anotherThing.setPlacement("PLACEMENT");
        anotherThing.setId("id4");

        return Arrays.asList(door, light, player, anotherThing);
    }
}
