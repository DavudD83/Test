package space.dotcat.assistant.repository.thingsRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.content.CommandArgs;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.LocalThingsSource;
import space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource.RemoteThingsSource;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ThingsRepositoryTest {

    @Mock
    private LocalThingsSource mLocalThingsSource;

    @Mock
    private RemoteThingsSource mRemoteThingsSource;

    private ThingRepository mThingRepository;

    private final String ROOM_ID = "R1";

    public final String THING_ID = "Li1";

    private Message MESSAGE = new Message("toggle", new CommandArgs());

    private final List<Thing> EMPTY_THINGS = new ArrayList<>();

    private final Flowable<List<Thing>> EMPTY_FLOWABLE_THINGS = Flowable.just(EMPTY_THINGS);

    private final List<Thing> THINGS = Arrays.asList(new Thing(), new Thing(), new Thing());

    private final Flowable<List<Thing>> FLOWABLE_THINGS = Flowable.just(THINGS);

    private final Throwable ERROR = new Throwable();

    private final Flowable<List<Thing>> ERROR_FLOWABLE_THINGS = Flowable.error(ERROR);

    private final ResponseActionMessage RESPONSE_ACTION = new ResponseActionMessage("successful");

    private final Single<ResponseActionMessage> SINGLE_RESPONSE_ACTION = Single.just(RESPONSE_ACTION);

    private final Single<ResponseActionMessage> ERROR_SINGLE_RESPONSE = Single.error(ERROR);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mThingRepository = new ThingRepositoryImpl(mLocalThingsSource, mRemoteThingsSource);
    }

    @After
    public void clear() {
        mLocalThingsSource = null;

        mRemoteThingsSource = null;

        mThingRepository = null;
    }

    @Test
    public void testRepoSuccessfullyCreated() {
        assertNotNull(mThingRepository);
    }

    @Test
    public void testGetThingsWhenThereIsNoLocalThings() {
        when(mLocalThingsSource.getThingsById(ROOM_ID)).thenReturn(EMPTY_FLOWABLE_THINGS);

        when(mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)).thenReturn(FLOWABLE_THINGS);

        mThingRepository.getThingsById(ROOM_ID)
                .test()
                .assertValue(THINGS);

        verify(mLocalThingsSource).getThingsById(ROOM_ID);

        verify(mRemoteThingsSource).loadThingsByPlacementId(ROOM_ID);

        verify(mLocalThingsSource).deleteAllThings();
        verify(mLocalThingsSource).addThingsSync(THINGS);
    }

    @Test
    public void testGetThingsWhenLocalThingsExist() {
        when(mLocalThingsSource.getThingsById(ROOM_ID)).thenReturn(FLOWABLE_THINGS);

        mThingRepository.getThingsById(ROOM_ID)
                .test()
                .assertValue(THINGS);

        verify(mLocalThingsSource).getThingsById(ROOM_ID);

        verifyNoMoreInteractions(mRemoteThingsSource);
    }

    @Test
    public void testGetThingsWithErrorWhenThereIsNoLocalThings() {
        when(mLocalThingsSource.getThingsById(ROOM_ID)).thenReturn(EMPTY_FLOWABLE_THINGS);

        when(mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)).thenReturn(ERROR_FLOWABLE_THINGS);

        mThingRepository.getThingsById(ROOM_ID)
                .test()
                .assertValue(EMPTY_THINGS)
                .assertError(ERROR);

        verify(mLocalThingsSource, times(2)).getThingsById(ROOM_ID);

        verify(mRemoteThingsSource).loadThingsByPlacementId(ROOM_ID);
    }

    @Test
    public void testRefreshThingsSuccessfully() {
        when(mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)).thenReturn(FLOWABLE_THINGS);

        mThingRepository.refreshThings(ROOM_ID)
                .test()
                .assertValue(THINGS);

        verify(mRemoteThingsSource).loadThingsByPlacementId(ROOM_ID);
    }

    @Test
    public void testRefreshThingsWithError() {
        when(mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)).thenReturn(ERROR_FLOWABLE_THINGS);

        mThingRepository.refreshThings(ROOM_ID)
                .test()
                .assertNoValues()
                .assertError(ERROR);

        verify(mRemoteThingsSource).loadThingsByPlacementId(ROOM_ID);
    }

    @Test
    public void testRefreshThingsSuccessfullyWithEmptyResponse() {
        when(mRemoteThingsSource.loadThingsByPlacementId(ROOM_ID)).thenReturn(EMPTY_FLOWABLE_THINGS);

        mThingRepository.refreshThings(ROOM_ID)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0)
                .assertNoErrors();

        verify(mRemoteThingsSource).loadThingsByPlacementId(ROOM_ID);

        verifyNoMoreInteractions(mLocalThingsSource);
    }

    @Test
    public void testDoActionSuccessfully() {
        when(mRemoteThingsSource.doActionOnThing(eq(THING_ID), any(Message.class))).thenReturn(SINGLE_RESPONSE_ACTION);

        mThingRepository.doAction(THING_ID, MESSAGE)
                .test()
                .assertNoErrors()
                .assertValue(RESPONSE_ACTION);

        verify(mRemoteThingsSource).doActionOnThing(THING_ID, MESSAGE);
    }

    @Test
    public void testDoActionWithError() {
        when(mRemoteThingsSource.doActionOnThing(eq(THING_ID), any(Message.class))).thenReturn(ERROR_SINGLE_RESPONSE);

        mThingRepository.doAction(THING_ID, MESSAGE)
                .test()
                .assertError(ERROR);

        verify(mRemoteThingsSource).doActionOnThing(THING_ID, MESSAGE);
    }

    @Test
    public void testUpdateThing() {
        Thing thing = new Thing();

        doNothing().when(mLocalThingsSource).updateThing(thing);

        mThingRepository.updateThing(thing)
                .test();

        verify(mLocalThingsSource).updateThing(thing);
    }

}
