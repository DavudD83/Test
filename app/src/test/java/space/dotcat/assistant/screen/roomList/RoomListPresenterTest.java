package space.dotcat.assistant.screen.roomList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class RoomListPresenterTest {

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    @Mock
    private RoomsViewContract mRoomsViewContract;

    @Mock
    private RoomRepository mRoomRepository;

    private RoomsPresenter mRoomsPresenter;

    private final Throwable ERROR = new Throwable();

    private final Flowable<List<Room>> API_ERROR = Flowable.error(ERROR);

    private final List<Room> ROOMS = createRoomList();

    private final Flowable<List<Room>> FLOWABLE_ROOMS = Flowable.just(ROOMS);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mRoomsPresenter = new RoomsPresenter(mRoomsViewContract, mRoomRepository, messageServiceHandler);
    }

    @After
    public void clear() {
        mRoomsViewContract = null;
        mRoomsPresenter = null;
        mRoomRepository = null;
    }

    @Test
    public void testPresenterCreated() {
        assertNotNull(mRoomsPresenter);
    }

    @Test
    public void testShowRoomsSuccess() {
        when(mRoomRepository.getRooms()).thenReturn(FLOWABLE_ROOMS);

        mRoomsPresenter.init();

        Mockito.verify(mRoomsViewContract).showLoading();
        Mockito.verify(mRoomsViewContract).hideLoading();
        Mockito.verify(mRoomsViewContract).showRooms(ROOMS);
    }

    @Test
    public void testShowRoomsError() {
        when(mRoomRepository.getRooms()).thenReturn(API_ERROR);

        mRoomsPresenter.init();

        Mockito.verify(mRoomsViewContract).showLoading();
        Mockito.verify(mRoomsViewContract).hideLoading();
        Mockito.verify(mRoomsViewContract).showError(ERROR);
    }

    @Test
    public void testRoomsEmptyError() {
        when(mRoomRepository.getRooms()).thenReturn(Flowable.just(new ArrayList<>()));

        mRoomsPresenter.init();

        Mockito.verify(mRoomsViewContract).showEmptyRoomsMessage();
    }

    @Test
    public void testRoomClick() {
        mRoomsPresenter.onItemClick(ROOMS.get(0));

        Mockito.verify(mRoomsViewContract).showRoomDetail(ROOMS.get(0));
    }

    @Test
    public void testReloadRoomsSuccess() {
        when(mRoomRepository.refreshRooms()).thenReturn(FLOWABLE_ROOMS);

        mRoomsPresenter.reloadRooms();

        Mockito.verifyNoMoreInteractions(mRoomsViewContract);
    }

    @Test
    public void testReloadRoomsError() {
        when(mRoomRepository.refreshRooms()).thenReturn(API_ERROR);

        mRoomsPresenter.reloadRooms();

        Mockito.verify(mRoomsViewContract).showError(ERROR);
    }

    @Test
    public void testScenario() {
        when(mRoomRepository.getRooms()).thenReturn(FLOWABLE_ROOMS);

        mRoomsPresenter.init();

        Mockito.verify(mRoomsViewContract).showRooms(ROOMS);

        mRoomsPresenter.onItemClick(ROOMS.get(1));

        Mockito.verify(mRoomsViewContract).showRoomDetail(ROOMS.get(1));
    }

    private List<Room> createRoomList() {
        List<Room> list = new ArrayList<>();

        list.add(new Room());
        list.add(new Room());
        list.add(new Room());

        return list;
    }
}
