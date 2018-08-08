package space.dotcat.assistant.screen.roomDetail;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.base.BasePresenterTest;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class RoomDetailsPresenterTest extends BasePresenterTest<RoomDetailsPresenter> {

    @Mock
    private RoomDetailsViewContract mRoomDetailsViewContract;

    @Mock
    private ThingRepository mThingRepository;

    private final String ROOM_ID = "R1";

    private final List<Thing> mThings = createThings();

    private final Flowable<List<Thing>> FLOWABLE_LIST = Flowable.just(mThings);

    private final Throwable API_ERROR = new Throwable();

    private final Flowable<List<Thing>> FLOWABLE_ERROR = Flowable.error(API_ERROR);

    private final Single<ResponseActionMessage> SINGLE_SUCCESSFULL_RESPONSE =
            Single.just(new ResponseActionMessage("accepted"));

    private final Single<ResponseActionMessage> SINGLE_ERROR = Single.error(API_ERROR);

    private final Completable SUCCESSFULL_COMPLETABLE = Completable.complete();

    private final List<Thing> EMPTY_THINGS = new ArrayList<>();

    private final Flowable<List<Thing>> EMPTY_FLOWABLE = Flowable.just(EMPTY_THINGS);

    @Override
    protected RoomDetailsPresenter createPresenterForTesting() {
        return new RoomDetailsPresenter(mRoomDetailsViewContract, mThingRepository);
    }

    @Test
    public void testPresenterCreated() {
        assertNotNull(mPresenter);
    }

    @Test
    public void testLoadThingsSuccessfully() {
        when(mThingRepository.getThingsById(ROOM_ID)).thenReturn(FLOWABLE_LIST);

        mPresenter.init(ROOM_ID);

        Mockito.verify(mRoomDetailsViewContract).showLoading();
        Mockito.verify(mRoomDetailsViewContract).hideLoading();
        Mockito.verify(mRoomDetailsViewContract).showThings(mThings);
    }

    @Test
    public void testLoadEmptyListOfThingsSuccessfully() {
        when(mThingRepository.getThingsById(ROOM_ID)).thenReturn(EMPTY_FLOWABLE);
        when(mThingRepository.refreshThings(ROOM_ID)).thenReturn(FLOWABLE_LIST);

        mPresenter.init(ROOM_ID);

        Mockito.verify(mThingRepository).refreshThings(ROOM_ID);

        Mockito.verify(mRoomDetailsViewContract).showLoading();
        Mockito.verify(mRoomDetailsViewContract).hideLoading();
        Mockito.verify(mRoomDetailsViewContract).showEmptyThingsError();
    }

    @Test
    public void testLoadThingsWithErrorWhileThereIsNoThingsLocally() {
        when(mThingRepository.getThingsById(ROOM_ID)).thenReturn(EMPTY_FLOWABLE);
        when(mThingRepository.refreshThings(ROOM_ID)).thenReturn(FLOWABLE_ERROR);

        mPresenter.init(ROOM_ID);

        Mockito.verify(mThingRepository).refreshThings(ROOM_ID);

        Mockito.verify(mRoomDetailsViewContract).hideLoading();
        Mockito.verify(mRoomDetailsViewContract).showEmptyThingsError();
        Mockito.verify(mRoomDetailsViewContract).showError(API_ERROR);
    }

    @Test
    public void testLoadThingsWithError() {
        when(mThingRepository.getThingsById(ROOM_ID)).thenReturn(FLOWABLE_ERROR);

        mPresenter.init(ROOM_ID);

        Mockito.verify(mRoomDetailsViewContract).showLoading();
        Mockito.verify(mRoomDetailsViewContract).hideLoading();
        Mockito.verify(mRoomDetailsViewContract).showError(API_ERROR);
    }

    @Test
    public void testReloadThingsSuccessfully() {
        when(mThingRepository.refreshThings(ROOM_ID)).thenReturn(FLOWABLE_LIST);

        mPresenter.reloadThings(ROOM_ID);

        Mockito.verifyNoMoreInteractions(mRoomDetailsViewContract);
    }

    @Test
    public void testReloadThingsFail() {
        when(mThingRepository.refreshThings(ROOM_ID)).thenReturn(FLOWABLE_ERROR);

        mPresenter.reloadThings(ROOM_ID);

        Mockito.verify(mRoomDetailsViewContract, Mockito.times(0)).showLoading();
        Mockito.verify(mRoomDetailsViewContract, Mockito.times(0)).hideLoading();

        Mockito.verify(mRoomDetailsViewContract).showError(API_ERROR);
    }

    @Test
    public void testThingToggledSuccessfully() {
        when(mThingRepository.doAction(eq(ROOM_ID), any(Message.class))).thenReturn(SINGLE_SUCCESSFULL_RESPONSE);

        when(mThingRepository.updateThing(any(Thing.class))).thenReturn(SUCCESSFULL_COMPLETABLE);

        Thing thing = mThings.get(0);

        mPresenter.onItemChange(thing);

        Mockito.verify(mRoomDetailsViewContract).showLoading();
        Mockito.verify(mRoomDetailsViewContract).hideLoading();
        Mockito.verify(mRoomDetailsViewContract, Mockito.times(0)).showError(API_ERROR);

//        thing.setActive(true);

        Mockito.verify(mThingRepository).updateThing(thing);
    }

    @Test
    public void testThingToggledWithFail() {
        Thing thing = mThings.get(1);

        when(mThingRepository.doAction(eq(ROOM_ID), any(Message.class))).thenReturn(SINGLE_ERROR);

        when(mThingRepository.updateThing(any(Thing.class))).thenReturn(SUCCESSFULL_COMPLETABLE);

        mPresenter.onItemChange(thing);

        Mockito.verify(mRoomDetailsViewContract).showError(API_ERROR);

        Mockito.verify(mThingRepository).updateThing(thing);
    }

    @Test
    public void testScenario() {
        when(mThingRepository.getThingsById(ROOM_ID)).thenReturn(FLOWABLE_ERROR);

        mPresenter.init(ROOM_ID);

        Mockito.verify(mRoomDetailsViewContract).showLoading();
        Mockito.verify(mRoomDetailsViewContract).hideLoading();
        Mockito.verify(mRoomDetailsViewContract).showError(API_ERROR);

        when(mThingRepository.refreshThings(ROOM_ID)).thenReturn(FLOWABLE_LIST);

        mPresenter.reloadThings(ROOM_ID);

        Thing thing = mThings.get(2);

        when(mThingRepository.doAction(eq(ROOM_ID), any(Message.class))).thenReturn(SINGLE_ERROR);

        when(mThingRepository.updateThing(thing)).thenReturn(SUCCESSFULL_COMPLETABLE);

        mPresenter.onItemChange(thing);

        Mockito.verify(mRoomDetailsViewContract, Mockito.times(2)).showLoading();
        Mockito.verify(mRoomDetailsViewContract, Mockito.times(2)).hideLoading();

        Mockito.verify(mThingRepository).updateThing(thing);

        Mockito.verify(mRoomDetailsViewContract, Mockito.times(2)).showError(API_ERROR);

        when(mThingRepository.doAction(eq(ROOM_ID), any(Message.class))).thenReturn(SINGLE_SUCCESSFULL_RESPONSE);

//        thing.setActive(false);

        mPresenter.onItemChange(thing);

        Mockito.verify(mRoomDetailsViewContract, Mockito.times(2)).showError(API_ERROR);
    }

    private List<Thing> createThings() {
        List<Thing> list = new ArrayList<>();

        list.add(new Thing());
        list.add(new Thing());
        list.add(new Thing());

        for(Thing t : list){
            t.setId(ROOM_ID);
        }

//        list.get(0).setActive(false);
//        list.get(1).setActive(false);
//        list.get(2).setActive(true);

        return list;
    }
}
