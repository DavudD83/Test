package space.dotcat.assistant.screen.auth;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.reactivex.Single;
import space.dotcat.assistant.base.BasePresenterTest;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.repository.authRepository.AuthRepository;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthPresenterTest extends BasePresenterTest<AuthPresenter> {

    @Mock
    private AuthViewContract mAuthViewContract;

    @Mock
    private AuthRepository mAuthRepository;

    private static final String URL = "https://api.ks-cube.tk/";

    private static final Single<AuthorizationAnswer> API_SUCCESS = Single.just(new AuthorizationAnswer("success",
            " token"));

    private static final Throwable ERROR = new Throwable();

    private static final Single<AuthorizationAnswer> API_ERROR = Single.error(ERROR);

    @Override
    protected AuthPresenter createPresenterForTesting() {
        return new AuthPresenter(mAuthViewContract, mAuthRepository);
    }

    @Test
    public void testPresenterCreated() {
        assertNotNull(mPresenter);
    }

    @Test
    public void testEmptyToken() {
        when(mAuthRepository.getToken()).thenReturn("");

        mPresenter.init();

        Mockito.verifyNoMoreInteractions(mAuthViewContract);
    }

    @Test
    public void testOpenRoomList() {
        when(mAuthRepository.getToken()).thenReturn("token");

        mPresenter.init();

        Mockito.verify(mAuthViewContract).showRoomList();
    }

    @Test
    public void testEmptyUrl() {
        mPresenter.tryLogin("", "login", "password");

        Mockito.verify(mAuthViewContract).showUrlEmptyError();
    }

    @Test
    public void testIncorrectUrl() {
        mPresenter.tryLogin("htp://some-url/", "login", "password");

        Mockito.verify(mAuthViewContract).showUrlNotCorrectError();
    }

    @Test
    public void testEmptyLogin() {
        mPresenter.tryLogin("https://url/", "", "password");

        Mockito.verify(mAuthViewContract).showLoginError();
    }

    @Test
    public void testEmptyPassword() {
        mPresenter.tryLogin("https://url/", "login", "");

        Mockito.verify(mAuthViewContract).showPasswordError();
    }

    @Test
    public void testEmptyAllFields() {
        mPresenter.tryLogin("", "", "");

        Mockito.verify(mAuthViewContract).showUrlEmptyError();
    }

    @Test
    public void testSuccessAuth() {
        when(mAuthRepository.getToken()).thenReturn("");

        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mPresenter.tryLogin(URL, "login", "password");

        Mockito.verify(mAuthViewContract).showLoading();
        Mockito.verify(mAuthViewContract).hideLoading();
        Mockito.verify(mAuthViewContract).showRoomList();
    }

    @Test
    public void testErrorAuth() {
        when(mAuthRepository.authUser(any())).thenReturn(API_ERROR);

        mPresenter.tryLogin(URL, "qwerty", "12345");

        Mockito.verify(mAuthViewContract).showLoading();
        Mockito.verify(mAuthViewContract).hideLoading();
        Mockito.verify(mAuthViewContract).showAuthError(ERROR);
    }

    @Test
    public void testScenario() {
        when(mAuthRepository.getToken()).thenReturn("");

        when(mAuthRepository.authUser(any())).thenReturn(API_ERROR);

        mPresenter.init();

        Mockito.verifyNoMoreInteractions(mAuthViewContract);

        mPresenter.tryLogin("url", "login", "pass");

        Mockito.verify(mAuthViewContract).showUrlNotCorrectError();

        mPresenter.tryLogin(URL, "login", "pass");

        Mockito.verify(mAuthViewContract, Mockito.times(1)).showLoading();
        Mockito.verify(mAuthViewContract, Mockito.times(1)).hideLoading();

        Mockito.verify(mAuthRepository).saveUrl(URL);
        Mockito.verify(mAuthRepository).destroyApiService();

        Mockito.verify(mAuthViewContract).showAuthError(ERROR);

        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mPresenter.tryLogin(URL, "login", "password");

        Mockito.verify(mAuthViewContract, Mockito.times(2)).showLoading();
        Mockito.verify(mAuthViewContract, Mockito.times(2)).hideLoading();

        Mockito.verify(mAuthRepository, times(2)).saveUrl(URL);
        Mockito.verify(mAuthRepository, times(2)).destroyApiService();

        Mockito.verify(mAuthViewContract).showRoomList();
    }

    @Test
    public void testExistingUrl() {
        when(mAuthRepository.getUrl()).thenReturn(URL);

        mPresenter.init();

        Mockito.verify(mAuthViewContract).showExistingUrl(URL);
    }

    @Test
    public void testSaveUrlAfterLogIn() {
        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mPresenter.tryLogin(URL, "login", "pass");

        Mockito.verify(mAuthRepository).saveUrl(URL);
    }

    @Test
    public void testDestroyApiInstanceAfterLogIn() {
        when(mAuthRepository.authUser(any())).thenReturn(API_SUCCESS);

        mPresenter.tryLogin(URL, "login", "pass");

        Mockito.verify(mAuthRepository).destroyApiService();
    }

    @Test
    public void testResetSetupState() {
        mPresenter.resetSetupState();

        verify(mAuthRepository).saveSetupState(false);
    }
}
