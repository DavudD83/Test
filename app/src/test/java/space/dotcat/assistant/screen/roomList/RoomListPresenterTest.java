package space.dotcat.assistant.screen.roomList;

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.testMock.MockApiRepository;

import static junit.framework.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class RoomListPresenterTest {

    private RoomsView mRoomsView;

    private RoomsPresenter mRoomsPresenter;

    private static final Throwable API_ERROR = new Throwable();

    private final List<Room> mRooms = createRoomList();

    @Before
    public void init() {
        mRoomsView = Mockito.mock(RoomsView.class);

        mRoomsPresenter = new RoomsPresenter(mRoomsView);
    }

    @After
    public void clear() {
        mRoomsView = null;
        mRoomsPresenter = null;
        RepositoryProvider.setApiRepository(null);
    }

    @Test
    public void testPresenterCreated() throws Exception {
        assertNotNull(mRoomsPresenter);
    }

    @Test
    public void testShowRoomsSuccess() throws Exception {
        RepositoryProvider.setApiRepository(new TestRoomsRepo(mRooms));

        mRoomsPresenter.init();

        Mockito.verify(mRoomsView).showLoading();
        Mockito.verify(mRoomsView).hideLoading();
        Mockito.verify(mRoomsView).showRooms(mRooms);
    }

    @Test
    public void testShowRoomsError() throws Exception {
        RepositoryProvider.setApiRepository(new TestRoomsRepo(null));

        mRoomsPresenter.init();

        Mockito.verify(mRoomsView).showLoading();
        Mockito.verify(mRoomsView).hideLoading();
        Mockito.verify(mRoomsView).showError(API_ERROR);
    }

    @Test
    public void testRoomClick() throws Exception {
        mRoomsPresenter.onItemClick(mRooms.get(0));

        Mockito.verify(mRoomsView).showRoomDetail(mRooms.get(0));
    }

    @Test
    public void testReloadRoomsSuccess() throws Exception {
        RepositoryProvider.setApiRepository(new TestRoomsRepo(mRooms));

        mRoomsPresenter.reloadData();

        Mockito.verify(mRoomsView).showRooms(mRooms);
    }

    @Test
    public void testReloadRoomsError() throws Exception {
        RepositoryProvider.setApiRepository(new TestRoomsRepo(null));

        mRoomsPresenter.reloadData();

        Mockito.verify(mRoomsView).showError(API_ERROR);
    }

    @Test
    public void testScenario() throws Exception {
        RepositoryProvider.setApiRepository(new TestRoomsRepo(null));

        mRoomsPresenter.init();

        Mockito.verify(mRoomsView).showError(API_ERROR);

        RepositoryProvider.setApiRepository(new TestRoomsRepo(mRooms));

        mRoomsPresenter.reloadData();

        Mockito.verify(mRoomsView).showRooms(mRooms);

        mRoomsPresenter.onItemClick(mRooms.get(1));

        Mockito.verify(mRoomsView).showRoomDetail(mRooms.get(1));
    }

    private List<Room> createRoomList(){
        List<Room> list = new ArrayList<>();

        list.add(new Room());
        list.add(new Room());
        list.add(new Room());

        return list;
    }

    private class TestRoomsRepo extends MockApiRepository {

        private List<Room> mRooms;

        TestRoomsRepo(List<Room> rooms) {
            mRooms = rooms;
        }

        @NonNull
        @Override
        public Observable<List<Room>> rooms() {
            if(mRooms != null){
                return Observable.just(mRooms);
            }

            return Observable.error(API_ERROR);
        }
    }
}
