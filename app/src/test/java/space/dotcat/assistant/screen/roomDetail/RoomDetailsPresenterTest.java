package space.dotcat.assistant.screen.roomDetail;

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
import io.reactivex.Single;
import space.dotcat.assistant.content.ActionParams;
import space.dotcat.assistant.content.Body;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.ApiRepository;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.testMock.MockApiRepository;

import static junit.framework.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class RoomDetailsPresenterTest {

    private RoomDetailsView mRoomDetailsView;

    private RoomDetailsPresenter mRoomDetailsPresenter;

    private static final String ROOM_ID = "R1";

    private final List<Thing> mThings = createThings();

    private static final Throwable API_ERROR = new Throwable();

    @Before
    public void init() {
        mRoomDetailsView = Mockito.mock(RoomDetailsView.class);

        mRoomDetailsPresenter = new RoomDetailsPresenter(mRoomDetailsView);
    }

    @After
    public void clear() {
        mRoomDetailsPresenter = null;
        mRoomDetailsView = null;
        RepositoryProvider.setApiRepository(null);
    }

    @Test
    public void testPresenterCreated() throws Exception {
        assertNotNull(mRoomDetailsPresenter);
    }

    @Test
    public void testLoadThingsSuccess() throws Exception {
        ApiRepository apiRepository = new TestApiRepoThings(false, mThings);

        RepositoryProvider.setApiRepository(apiRepository);

        mRoomDetailsPresenter.init(ROOM_ID);

        Mockito.verify(mRoomDetailsView).showLoading();
        Mockito.verify(mRoomDetailsView).hideLoading();
        Mockito.verify(mRoomDetailsView).showThings(mThings);
    }

    @Test
    public void testLoadThingsFail() throws Exception {
        ApiRepository apiRepository = new TestApiRepoThings(true, mThings);

        RepositoryProvider.setApiRepository(apiRepository);

        mRoomDetailsPresenter.init(ROOM_ID);

        Mockito.verify(mRoomDetailsView).showLoading();
        Mockito.verify(mRoomDetailsView).hideLoading();
        Mockito.verify(mRoomDetailsView).showError(API_ERROR);
    }

    @Test
    public void testReloadThingsSuccess() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepoThings(false, mThings));
        mRoomDetailsPresenter.reloadData(ROOM_ID);

        Mockito.verify(mRoomDetailsView, Mockito.times(0)).showLoading();
        Mockito.verify(mRoomDetailsView, Mockito.times(0)).hideLoading();

        Mockito.verify(mRoomDetailsView).showThings(mThings);
    }

    @Test
    public void testReloadThingsFail() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepoThings(true, mThings));
        mRoomDetailsPresenter.reloadData(ROOM_ID);

        Mockito.verify(mRoomDetailsView, Mockito.times(0)).showLoading();
        Mockito.verify(mRoomDetailsView, Mockito.times(0)).hideLoading();

        Mockito.verify(mRoomDetailsView).showError(API_ERROR);
    }

    @Test
    public void testThingToggledSuccess() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepoAction(false));

        mRoomDetailsPresenter.onItemChange(mThings.get(0));

        Mockito.verify(mRoomDetailsView, Mockito.times(0)).showError(API_ERROR);
    }

    @Test
    public void testThingToggledFail() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepoAction(true));

        mRoomDetailsPresenter.onItemChange(mThings.get(1));

        Mockito.verify(mRoomDetailsView).showError(API_ERROR);
    }

    @Test
    public void testScenario() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepoThings(true, mThings));

        mRoomDetailsPresenter.init(ROOM_ID);

        Mockito.verify(mRoomDetailsView).showLoading();
        Mockito.verify(mRoomDetailsView).hideLoading();
        Mockito.verify(mRoomDetailsView).showError(API_ERROR);

        RepositoryProvider.setApiRepository(new TestApiRepoThings(false, mThings));

        mRoomDetailsPresenter.reloadData(ROOM_ID);

        Mockito.verify(mRoomDetailsView, Mockito.times(1)).showLoading();
        Mockito.verify(mRoomDetailsView, Mockito.times(1)).hideLoading();
        Mockito.verify(mRoomDetailsView).showThings(mThings);

        RepositoryProvider.setApiRepository(new TestApiRepoAction(true));

        mRoomDetailsPresenter.onItemChange(mThings.get(0));

        Mockito.verify(mRoomDetailsView, Mockito.times(2)).showLoading();
        Mockito.verify(mRoomDetailsView, Mockito.times(2)).hideLoading();
        Mockito.verify(mRoomDetailsView, Mockito.times(2)).showError(API_ERROR);

        mRoomDetailsPresenter.reloadData(ROOM_ID);

        Mockito.verify(mRoomDetailsView, Mockito.times(2)).showLoading();
        Mockito.verify(mRoomDetailsView, Mockito.times(2)).hideLoading();
        Mockito.verify(mRoomDetailsView).showThings(mThings);

        RepositoryProvider.setApiRepository(new TestApiRepoAction(false));

        mRoomDetailsPresenter.onItemChange(mThings.get(0));

        Mockito.verify(mRoomDetailsView, Mockito.times(2)).showError(API_ERROR);
    }

    private List<Thing> createThings(){
        List<Thing> list = new ArrayList<>();

        list.add(new Thing());
        list.add(new Thing());
        list.add(new Thing());

        for(Thing t : list){
            t.setId(ROOM_ID);
        }

        return list;
    }

    private class TestApiRepoThings extends MockApiRepository {

        private boolean mIsError;

        private List<Thing> mThings;

        TestApiRepoThings(boolean isError, List<Thing> things) {
            mIsError = isError;
            mThings = things;
        }

        @NonNull
        @Override
        public Observable<List<Thing>> things(String id) {
            if(!mIsError){
                return Observable.just(mThings);
            }

            return Observable.error(API_ERROR);
        }
    }

    private class TestApiRepoAction extends MockApiRepository {

        private boolean mIsError;

        private final String mCommand = "toggle";

        TestApiRepoAction(boolean isError) {
            mIsError = isError;
        }

        @NonNull
        @Override
        public Single<ResponseActionMessage> action(@NonNull String id, @NonNull Message message) {
            if(!mIsError) {
                if (message.getCommand().equals(mCommand) && message.getCommandArgs() != null) {
                    return Single.just(new ResponseActionMessage("accepted"));
                }

                return Single.error(API_ERROR);
            }

            return Single.error(API_ERROR);
        }
    }
}
