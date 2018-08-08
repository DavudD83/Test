package space.dotcat.assistant.screen.roomList;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.assistant.base.BasePresenterTest;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.service.ServiceHandler;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RoomListPresenterTest extends BasePresenterTest<RoomsPresenter> {

    @Mock
    private RoomsViewContract mRoomsViewContract;

    @Mock
    private RoomRepository mRoomRepository;

    @Mock
    private ServiceHandler messageServiceHandler;

    private final Throwable ERROR = new Throwable();

    private final Flowable<List<Room>> API_ERROR = Flowable.error(ERROR);

    private final List<Room> ROOMS = createRoomList();

    private final Flowable<List<Room>> FLOWABLE_ROOMS = Flowable.just(ROOMS);

    private final List<Room> EMPTY_ROOMS = new ArrayList<>();

    private final Flowable<List<Room>> FLOWABLE_EMPTY_ROOMS = Flowable.just(EMPTY_ROOMS);

    @Override
    protected RoomsPresenter createPresenterForTesting() {
        return new RoomsPresenter(mRoomsViewContract, mRoomRepository, messageServiceHandler);
    }

    @Test
    public void testPresenterCreated() {
        assertNotNull(mPresenter);
    }

    @Test
    public void testInitSuccessful() {
        when(mRoomRepository.getRooms()).thenReturn(FLOWABLE_ROOMS);

        mPresenter.init();

        verify(mRoomsViewContract).showLoading();
        verify(mRoomsViewContract).hideLoading();
        verify(mRoomsViewContract).showRooms(ROOMS);

        verify(messageServiceHandler).startService();
    }

    @Test
    public void testInitError() {
        when(mRoomRepository.getRooms()).thenReturn(API_ERROR);

        mPresenter.init();

        verify(mRoomsViewContract).showLoading();
        verify(mRoomsViewContract).hideLoading();
        verify(mRoomsViewContract).showError(ERROR);
    }

    @Test
    public void testRoomsEmptyError() {
        when(mRoomRepository.getRooms()).thenReturn(Flowable.just(new ArrayList<>()));
        when(mRoomRepository.refreshRooms()).thenReturn(Flowable.just(new ArrayList<>()));

        mPresenter.init();

        verify(mRoomRepository).refreshRooms();

        verify(mRoomsViewContract).hideLoading();

        verify(mRoomsViewContract).showEmptyRoomsMessage();
    }

    @Test
    public void testErrorWhenRoomsEmptyLocally() {
        when(mRoomRepository.getRooms()).thenReturn(FLOWABLE_EMPTY_ROOMS);
        when(mRoomRepository.refreshRooms()).thenReturn(API_ERROR);

        mPresenter.init();

        verify(mRoomRepository).getRooms();

        verify(mRoomRepository).refreshRooms();

        verify(mRoomsViewContract).showError(ERROR);
        verify(mRoomsViewContract).hideLoading();
        verify(mRoomsViewContract).showEmptyRoomsMessage();
    }

    @Test
    public void testRoomClick() {
        mPresenter.onItemClick(ROOMS.get(0));

        verify(mRoomsViewContract).showRoomDetail(ROOMS.get(0));
    }

    @Test
    public void testReloadRoomsSuccess() {
        when(mRoomRepository.refreshRooms()).thenReturn(FLOWABLE_ROOMS);

        mPresenter.reloadRooms();

        verifyNoMoreInteractions(mRoomsViewContract);
    }

    @Test
    public void testReloadRoomsError() {
        when(mRoomRepository.refreshRooms()).thenReturn(API_ERROR);

        mPresenter.reloadRooms();

        verify(mRoomsViewContract).showError(ERROR);
    }

    @Test
    public void testScenario() {
        when(mRoomRepository.getRooms()).thenReturn(FLOWABLE_ROOMS);

        mPresenter.init();

        verify(mRoomsViewContract).showRooms(ROOMS);

        verify(messageServiceHandler).startService();

        mPresenter.onItemClick(ROOMS.get(1));

        verify(mRoomsViewContract).showRoomDetail(ROOMS.get(1));
    }

    private List<Room> createRoomList() {
        List<Room> list = new ArrayList<>();

        list.add(new Room());
        list.add(new Room());
        list.add(new Room());

        return list;
    }
}
