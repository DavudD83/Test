package space.dotcat.assistant.repository.roomsRepository;

import android.support.test.InstrumentationRegistry;
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
import space.dotcat.assistant.di.appComponent.DaggerAppComponent;
import space.dotcat.assistant.di.appComponent.SharedPreferencesModule;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.roomsRepository.remoteRoomsDataSource.RemoteRoomsSource;
import space.dotcat.assistant.utils.RxJavaTestRule;

@RunWith(AndroidJUnit4.class)
public class RemoteRoomsSourceTest {

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    private LocalAuthSource mLocalAuthSource;

    private RemoteRoomsSource mRemoteRoomsSource;

    @Before
    public void init() {
        mLocalAuthSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeLocalAuthSource();

        mRemoteRoomsSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeRemoteRoomsSource();
    }

    @After
    public void clear() {
        mLocalAuthSource.deleteToken();

        mLocalAuthSource = null;

        mRemoteRoomsSource = null;
    }

    @Test
    public void getRoomsSuccessfully() {
        mRemoteRoomsSource.getRooms()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(6)
                .assertNoErrors()
                .assertValueAt(0, room -> room.getFriendlyName().equals("Corridor"))
                .assertValueAt(1, room -> room.getFriendlyName().equals("Kitchen"))
                .assertValueAt(2, room -> room.getFriendlyName().equals("Bathroom"));
    }

    @Test
    public void getEmptyRooms() {
        mLocalAuthSource.saveToken(RequestMatcher.ERROR_EMPTY_ROOMS);

        mRemoteRoomsSource.getRooms()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertNoErrors()
                .assertValueCount(0);
    }

    @Test
    public void getRoomsWithError() {
        mLocalAuthSource.saveToken(RequestMatcher.ERROR);

        mRemoteRoomsSource.getRooms()
                .test()
                .assertError(ApiError.class);
    }

}
