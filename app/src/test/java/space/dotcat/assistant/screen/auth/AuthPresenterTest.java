package space.dotcat.assistant.screen.auth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class AuthPresenterTest {

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    @Mock
    private AuthViewContract mAuthViewContract;

    @Mock
    private AuthRepository mAuthRepository;

    private AuthPresenter mAuthPresenter;

    private static final String URL = "https://api.ks-cube.tk/";

    private static final Single<AuthorizationAnswer> API_SUCCESS = Single.just(new AuthorizationAnswer("success",
            " token"));

    private static final Throwable ERROR = new Throwable();

    private static final Single<AuthorizationAnswer> API_ERROR = Single.error(ERROR);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mAuthPresenter = new AuthPresenter(mAuthViewContract, mAuthRepository);
    }

    @After
    public void clear() {
        mAuthViewContract = null;
        mAuthRepository = null;
        mAuthPresenter = null;
    }

    @Test
    public void testPresenterCreated() {
        assertNotNull(mAuthPresenter);
    }

    @Test
    public void testEmptyToken() {
        when(mAuthRepository.getToken()).thenReturn("");

        mAuthPresenter.init();

        Mockito.verifyNoMoreInteractions(mAuthViewContract);
    }

    @Test
    public void testOpenRoomList() {
        when(mAuthRepository.getToken()).thenReturn("token");

        mAuthPresenter.init();

        Mockito.verify(mAuthViewContract).showRoomList();
    }

    @Test
    public void testEmptyUrl() {
        mAuthPresenter.tryLogin("", "login", "password");

        Mockito.verify(mAuthViewContract).showUrlEmptyError();
    }

    @Test
    public void testIncorrectUrl() {
        mAuthPresenter.tryLogin("htp://some-url/", "login", "password");

        Mockito.verify(mAuthViewContract).showUrlNotCorrectError();
    }

    @Test
    public void testEmptyLogin() {
        mAuthPresenter.tryLogin("https://url/", "", "password");

        Mockito.verify(mAuthViewContract).showLoginError();
    }

    @Test
    public void testEmptyPassword() {
        mAuthPresenter.tryLogin("https://url/", "login", "");

        Mockito.verify(mAuthViewContract).showPasswordError();
    }

    @Test
    public void testEmptyAllFields() {
        mAuthPresenter.tryLogin("", "", "");

        Mockito.verify(mAuthViewContract).showUrlEmptyError();
    }

    @Test
    public void testSuccessAuth() {
        when(mAuthRepository.getToken()).thenReturn("");

        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mAuthPresenter.tryLogin(URL, "login", "password");

        Mockito.verify(mAuthViewContract).showLoading();
        Mockito.verify(mAuthViewContract).hideLoading();
        Mockito.verify(mAuthViewContract).showRoomList();
    }

    @Test
    public void testErrorAuth() {
        when(mAuthRepository.authUser(any())).thenReturn(API_ERROR);

        mAuthPresenter.tryLogin(URL, "qwerty", "12345");

        Mockito.verify(mAuthViewContract).showLoading();
        Mockito.verify(mAuthViewContract).hideLoading();
        Mockito.verify(mAuthViewContract).showAuthError(ERROR);
    }

    @Test
    public void testScenario() {
        when(mAuthRepository.getToken()).thenReturn("");

        when(mAuthRepository.authUser(any())).thenReturn(API_ERROR);

        mAuthPresenter.init();

        Mockito.verifyNoMoreInteractions(mAuthViewContract);

        mAuthPresenter.tryLogin("url", "login", "pass");

        Mockito.verify(mAuthViewContract).showUrlNotCorrectError();

        mAuthPresenter.tryLogin(URL, "login", "pass");

        Mockito.verify(mAuthViewContract, Mockito.times(1)).showLoading();
        Mockito.verify(mAuthViewContract, Mockito.times(1)).hideLoading();

        Mockito.verify(mAuthRepository).saveUrl(URL);
        Mockito.verify(mAuthRepository).destroyApiService();

        Mockito.verify(mAuthViewContract).showAuthError(ERROR);

        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mAuthPresenter.tryLogin(URL, "login", "password");

        Mockito.verify(mAuthViewContract, Mockito.times(2)).showLoading();
        Mockito.verify(mAuthViewContract, Mockito.times(2)).hideLoading();

        Mockito.verify(mAuthRepository, times(2)).saveUrl(URL);
        Mockito.verify(mAuthRepository, times(2)).destroyApiService();

        Mockito.verify(mAuthViewContract).showRoomList();
    }

    @Test
    public void testExistingUrl() {
        when(mAuthRepository.getUrl()).thenReturn(URL);

        mAuthPresenter.init();

        Mockito.verify(mAuthViewContract).showExistingUrl(URL);
    }

    @Test
    public void testSaveUrlAfterLogIn() {
        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mAuthPresenter.tryLogin(URL, "login", "pass");

        Mockito.verify(mAuthRepository).saveUrl(URL);
    }

    @Test
    public void testDestroyApiInstanceAfterLogIn() {
        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mAuthPresenter.tryLogin(URL, "login", "pass");

        Mockito.verify(mAuthRepository).destroyApiService();
    }
}
