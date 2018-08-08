package space.dotcat.assistant.repository.thingsRepository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;
import io.reactivex.subscribers.TestSubscriber;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.RGB;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.di.appComponent.DaggerAppComponent;
import space.dotcat.assistant.repository.AppDatabase;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.ThingsDao;

@RunWith(AndroidJUnit4.class)
public class ThingsDaoTest {

    @Rule
    public InstantTaskExecutorRule mTaskExecutorRule = new InstantTaskExecutorRule();

    private ThingsDao mThingsDao;

    private static final List<Thing> THINGS = createThings();

    private static final String PLACEMENT_ID = "R1";

    private TestSubscriber mTestSubscriber;

    @Before
    public void init() {
        mThingsDao = AppDelegate.getInstance().getAppComponent().getFakeThingsDao();

        mTestSubscriber = new TestSubscriber();

        mThingsDao.deleteAllThings();
    }

    public void after() {
        mTestSubscriber = null;

        mThingsDao.deleteAllThings();
    }

    @Test
    public void testInsertThings() {
        mThingsDao.getAllThingsById(PLACEMENT_ID)
                .flatMap(Flowable::fromIterable)
                .subscribe(mTestSubscriber);

        mTestSubscriber.assertValueCount(0)
                .assertNoErrors()
                .assertNotComplete();

        mThingsDao.insertThings(THINGS);

        mTestSubscriber.assertValueCount(15)
                .assertNoErrors()
                .assertNotComplete();
    }

    @Test
    public void testObservableQuery() {
        mThingsDao.getDimmableLamps(PLACEMENT_ID)
                .flatMap(Flowable::fromIterable)
                .subscribe(mTestSubscriber);

        mTestSubscriber.assertNoErrors();
        mTestSubscriber.assertValueCount(0);

        DimmableLamp dimmableLamp = new DimmableLamp();
        dimmableLamp.setId("Li1");
        dimmableLamp.setPlacement(PLACEMENT_ID);
        dimmableLamp.setPoweredOn(false);

        mThingsDao.insertThingByType(dimmableLamp);

        mTestSubscriber.assertValueCount(1);
        mTestSubscriber.assertValue((Predicate<DimmableLamp>) dl -> dl.getPlacement().equals(PLACEMENT_ID) && dl.getId().equals("Li1"));

        dimmableLamp.setPoweredOn(true);

        mThingsDao.updateThingByType(dimmableLamp);

        mTestSubscriber.assertValueAt(0, (Predicate<DimmableLamp>) dl-> dl.getId().equals("Li1"));
        mTestSubscriber.assertValueAt(1, (Predicate<DimmableLamp>) dl -> dl.getId().equals("Li1") && dl.getIsPoweredOn());
    }

    @Test
    public void testObservableWithFlowableZip() {
        mThingsDao.getAllThings(PLACEMENT_ID)
                .flatMap(Flowable::fromIterable)
                .subscribe(mTestSubscriber);

        mTestSubscriber.assertValueCount(0);
        mTestSubscriber.assertNoErrors();

        mThingsDao.insertThings(THINGS);

        mTestSubscriber.assertValueCount(15);
    }

    private static List<Thing> createThings() {
        List<Thing> things = new ArrayList<>();

        Thing doorLock = new DoorLock("0", new ArrayList<>(), new ArrayList<>(), true, false,
                PLACEMENT_ID, "door_lock", "door", "opened", true);

        Thing lamp = new Lamp("0", new ArrayList<>(), new ArrayList<>(), true, false,
                PLACEMENT_ID, "door_lock", "door", "opened", true, false);

        Thing dimmableLamp = new DimmableLamp("0", new ArrayList<>(), new ArrayList<>(), true, false,
                PLACEMENT_ID, "door_lock", "door", "opened", false, true, 50);

        Thing colorTemperatureLamp = new ColorTemperatureLamp("0", new ArrayList<>(), new ArrayList<>(), true, false,
                PLACEMENT_ID, "door_lock", "door", "opened", false, true, 50, 70);

        Thing rgbLamp = new RGBLamp("0", new ArrayList<>(), new ArrayList<>(), true, false,
                PLACEMENT_ID, "door_lock", "door", "opened", false, true, 50, 70,
                new RGB(0, 0,0 ), 100.0f, 100.0f);

        things.add(doorLock);
        things.add(lamp);
        things.add(dimmableLamp);
        things.add(colorTemperatureLamp);
        things.add(rgbLamp);

        return things;
    }
}
