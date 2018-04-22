package space.dotcat.assistant.repository.thingsRepository;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Flowable;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.api.RequestMatcher;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.content.CommandArgs;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource.RemoteThingsSource;
import space.dotcat.assistant.utils.RxJavaTestRule;

@RunWith(AndroidJUnit4.class)
public class RemoteThingsSourceTest {

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    private LocalAuthSource mLocalAuthSource;

    private RemoteThingsSource mRemoteThingsSource;

    private final String ROOM_ID = "R1";

    private final String THING_ID = "D1";

    private final Message MESSAGE = new Message("toggle", new CommandArgs());

    @Before
    public void init() {
        mLocalAuthSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeLocalAuthSource();

        mRemoteThingsSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeRemoteThingsSource();
    }

    @After
    public void clear() {
        mLocalAuthSource.deleteToken();

        mRemoteThingsSource = null;

        mLocalAuthSource = null;
    }

    @Test
    public void testLoadThingsByPlacementId() {
        mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .filter(thing -> thing.getPlacement().equals(ROOM_ID))
                .test()
                .assertValueCount(2);
    }

    @Test
    public void testLoadEmptyThings() {
        mLocalAuthSource.saveToken(RequestMatcher.ERROR_EMPTY_THINGS);

        mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0);
    }

    @Test
    public void testLoadThingsWithError() {
        mLocalAuthSource.saveToken(RequestMatcher.ERROR);

        mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)
                .test()
                .assertError(ApiError.class);
    }

    @Test
    public void testDoActionSuccessfully() {
        mRemoteThingsSource.doActionOnThing(THING_ID, MESSAGE)
                .test()
                .assertValue(m-> m.getMessage().equals("accepted"));
    }

    @Test
    public void testDoActionWithError() {
        mLocalAuthSource.saveToken(RequestMatcher.ERROR);

        mRemoteThingsSource.doActionOnThing(THING_ID, MESSAGE)
                .test()
                .assertError(ApiError.class);
    }
}
