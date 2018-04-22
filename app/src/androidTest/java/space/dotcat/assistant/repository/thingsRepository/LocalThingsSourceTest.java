package space.dotcat.assistant.repository.thingsRepository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.assistant.AppDelegate;
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
                .assertValueAt(0, thing -> !thing.getIsActive())
                .assertValueAt(1, thing -> !thing.getIsActive())
                .assertValueAt(2, Thing::getIsActive) ;
    }

    @Test
    public void testDeleteThings() {
        mLocalThingsSource.addThingsSync(THINGS);

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueAt(0, thing -> !thing.getIsActive());

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
        updatedThing.setActive(true);

        mLocalThingsSource.updateThing(updatedThing);
    }

    @Test
    public void testInsertThingsWithReplacing() {
        mLocalThingsSource.addThingsSync(THINGS);

        Thing thing = new Thing();
        thing.setId("id1");
        thing.setPlacement(ROOM_ID);
        thing.setActive(true);

        Thing thing1 = new Thing();
        thing1.setId("id2");
        thing1.setPlacement(ROOM_ID);
        thing1.setActive(false);

        mLocalThingsSource.addThingsSync(Arrays.asList(thing, thing1));

        mLocalThingsSource.getThingsById(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueAt(1, Thing::getIsActive)
                .assertValueAt(2, t-> !t.getIsActive());
    }


    private List<Thing> createThings() {
        Thing door = new Thing();

        door.setPlacement(ROOM_ID);
        door.setId("id");
        door.setActive(false);

        Thing light = new Thing();

        light.setPlacement(ROOM_ID);
        light.setId("id1");
        light.setActive(false);

        Thing player = new Thing();

        player.setPlacement(ROOM_ID);
        player.setId("id2");
        player.setActive(true);

        Thing anotherThing = new Thing();

        anotherThing.setPlacement("PLACEMENT");
        anotherThing.setId("id4");

        return Arrays.asList(door, light, player, anotherThing);
    }
}
