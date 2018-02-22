package space.dotcat.assistant.repository.apiRepository;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.realm.Realm;
import space.dotcat.assistant.api.OkHttpProvider;
import space.dotcat.assistant.content.ActionParams;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Body;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.ApiRepository;
import space.dotcat.assistant.repository.DefaultApiRepository;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ApiRepositoryTest {

    private ApiRepository mApiRepository;

    private final static String TOKEN = "90ff4ba085545c1735ab6c29a916f9cb8c0b7222";

    private final static AuthorizationAnswer ERROR =
            new AuthorizationAnswer("Error","ERROR");

    private final static String ROOM_ID = "R2";

    private final static Message MESSAGE =
            new Message(new Body("toggle", ROOM_ID, new ActionParams()));

    @Rule
    public final RxJavaTestRule mTestRule = new RxJavaTestRule();

    @Before
    public void init() {
        mApiRepository = new DefaultApiRepository();
        RepositoryProvider.provideAuthRepository().saveUrl(new Url("https://api.ks-cube.tk/"));
    }

    @After
    public void clear() {
        Realm.getDefaultInstance().executeTransaction(realm ->
        {
            realm.delete(Room.class);
            realm.delete(Thing.class);
        });

        RepositoryProvider.provideAuthRepository().deleteToken();

        mApiRepository = null;
    }

    @Test
    public void testRepositoryCreated() throws Exception {
        assertNotNull(mApiRepository);
    }

    @Test
    public void testSuccessAuth() throws Exception {
        AuthorizationAnswer answer = mApiRepository
                .auth(new Authorization("login", "pass")).blockingGet();

        assertEquals(TOKEN, answer.getToken());

        assertEquals(TOKEN, RepositoryProvider.provideAuthRepository().token());
    }

    @Test
    public void testErrorAuth() throws Exception {
        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR);

        mApiRepository.auth(new Authorization("login", "pass"))
                .test()
                .assertError(ApiError.class);

        assertTrue(OkHttpProvider.isClientDeleted());
    }

    @Test
    public void testLoadRooms() throws Exception {
        mApiRepository.rooms()
                .flatMap(Observable::fromIterable)
                .test()
                .assertValueCount(6)
                .assertNoErrors();
    }

    @Test
    public void testRoomsSavedInCache() throws Exception {
        mApiRepository.rooms().subscribe();

        int savedCount = Realm.getDefaultInstance()
                .where(Room.class)
                .findAll()
                .size();

        assertEquals(6, savedCount);
    }

    @Test
    public void testRoomsRestoredFromCache() throws Exception {
        mApiRepository.rooms().subscribe();

        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR);

        mApiRepository.rooms()
                .flatMap(Observable::fromIterable)
                .test()
                .assertValueCount(6)
                .assertError(ApiError.class);
    }

    @Test
    public void testLoadThings() throws Exception {
        mApiRepository.things(ROOM_ID)
                .flatMap(Observable::fromIterable)
                .test()
                .assertValueCount(3)
                .assertNoErrors();
    }

    @Test
    public void testThingsSavedInCache() throws Exception {
        mApiRepository.things(ROOM_ID).subscribe();

        int countSaved = Realm.getDefaultInstance()
                .where(Thing.class)
                .contains("mPlacement", ROOM_ID)
                .findAll()
                .size();

        assertEquals(3, countSaved);
    }

    @Test
    public void testThingsRestoredFromCache() throws Exception {
        mApiRepository.things(ROOM_ID).subscribe();

        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR);

        mApiRepository.things(ROOM_ID)
                .flatMap(Observable::fromIterable)
                .test()
                .assertError(ApiError.class)
                .assertValueCount(3);
    }

    @Test
    public void testDoAction() throws Exception {
        mApiRepository.action(MESSAGE).test().assertNoErrors();
    }

    @Test
    public void testActionWithError() throws Exception {
        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR);

        mApiRepository.action(MESSAGE)
                    .test()
                    .assertError(ApiError.class);
    }
}
