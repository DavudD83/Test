package space.dotcat.assistant.screen.auth;

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import ru.arturvasilov.rxloader.LifecycleHandler;
import rx.Observable;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.repository.AuthRepository;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.testMock.MockApiRepository;
import space.dotcat.assistant.testMock.MockAuthRepository;
import space.dotcat.assistant.testMock.MockLifecycleHandler;

import static junit.framework.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class AuthPresenterTest {

    private AuthView mAuthView;

    private LifecycleHandler mLifecycleHandler;

    private AuthPresenter mAuthPresenter;

    private static final String URL = "https://api.ks-cube.tk/";

    private static final Throwable API_ERROR = new Throwable();

    @Before
    public void init() {
        mAuthView = Mockito.mock(AuthView.class);

        mLifecycleHandler = new MockLifecycleHandler();

        mAuthPresenter = new AuthPresenter(mAuthView, mLifecycleHandler);
    }

    @After
    public void clear() {
        mLifecycleHandler = null;
        mAuthView = null;
        mAuthPresenter = null;

        RepositoryProvider.setAuthRepository(null);
        RepositoryProvider.setApiRepository(null);
    }

    public void testPresenterCreated() throws Exception {
        assertNotNull(mAuthPresenter);
    }

    @Test
    public void testEmptyToken() throws Exception {
        AuthRepository authRepository = new TestAuthRepo("");

        RepositoryProvider.setAuthRepository(authRepository);

        mAuthPresenter.init();

        Mockito.verifyNoMoreInteractions(mAuthView);
    }

    @Test
    public void testOpenRoomList() throws Exception {
        AuthRepository authRepository = new TestAuthRepo("token");

        RepositoryProvider.setAuthRepository(authRepository);

        mAuthPresenter.init();

        Mockito.verify(mAuthView).showRoomList();
    }

    @Test
    public void testEmptyUrl() throws Exception {
        mAuthPresenter.tryLogin("", "login", "password");

        Mockito.verify(mAuthView).showUrlEmptyError();
    }

    @Test
    public void testIncorrectUrl() throws Exception {
        mAuthPresenter.tryLogin("htp://some-url/", "login", "password");

        Mockito.verify(mAuthView).showUrlNotCorrectError();
    }

    @Test
    public void testEmptyLogin() throws Exception {
        mAuthPresenter.tryLogin("https://url/", "", "password");

        Mockito.verify(mAuthView).showLoginError();
    }

    @Test
    public void testEmptyPassword() throws Exception {
        mAuthPresenter.tryLogin("https://url/", "login", "");

        Mockito.verify(mAuthView).showPasswordError();
    }

    @Test
    public void testEmptyAllFields() throws Exception {
        mAuthPresenter.tryLogin("", "", "");

        Mockito.verify(mAuthView).showUrlEmptyError();
    }

    @Test
    public void testSuccessAuth() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepo());
        RepositoryProvider.setAuthRepository(new TestAuthRepo(""));

        mAuthPresenter.tryLogin(URL, "login", "password");

        Mockito.verify(mAuthView).showLoading();
        Mockito.verify(mAuthView).hideLoading();
        Mockito.verify(mAuthView).showRoomList();
    }

    @Test
    public void testErrorAuth() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepo());
        RepositoryProvider.setAuthRepository(new TestAuthRepo(""));

        mAuthPresenter.tryLogin(URL, "qwerty", "12345");

        Mockito.verify(mAuthView).showLoading();
        Mockito.verify(mAuthView).hideLoading();
        Mockito.verify(mAuthView).showAuthError(API_ERROR);
    }

    @Test
    public void testScenario() throws Exception {
        RepositoryProvider.setApiRepository(new TestApiRepo());
        RepositoryProvider.setAuthRepository(new TestAuthRepo(""));

        mAuthPresenter.init();

        Mockito.verifyNoMoreInteractions(mAuthView);

        mAuthPresenter.tryLogin("url", "login", "pass");

        Mockito.verify(mAuthView).showUrlNotCorrectError();

        mAuthPresenter.tryLogin(URL, "login", "pass");

        Mockito.verify(mAuthView, Mockito.times(1)).showLoading();
        Mockito.verify(mAuthView, Mockito.times(1)).hideLoading();
        Mockito.verify(mAuthView).showAuthError(API_ERROR);

        mAuthPresenter.tryLogin(URL, "login", "password");

        Mockito.verify(mAuthView, Mockito.times(2)).showLoading();
        Mockito.verify(mAuthView, Mockito.times(2)).hideLoading();
        Mockito.verify(mAuthView).showRoomList();
    }

    private class TestAuthRepo extends MockAuthRepository {

        private String mToken;

        TestAuthRepo(String token) {
            mToken = token;
        }

        @Override
        public String token() {
            return mToken;
        }
    }

    private class TestApiRepo extends MockApiRepository {

        @NonNull
        @Override
        public Observable<AuthorizationAnswer> auth(@NonNull Authorization authorizationInfo) {
            if(authorizationInfo.getUsername().equals("login") &&
                    authorizationInfo.getPassword().equals("password")){
                return Observable.just(new AuthorizationAnswer("success", "token"));
            }

            return Observable.error(API_ERROR);
        }
    }
}
