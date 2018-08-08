package space.dotcat.assistant.repository.authRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.authRepository.remoteDataSource.RemoteAuthSource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class AuthRepositoryTest {

    private static final boolean IS_SECURED = true;
    @Mock
    private LocalAuthSource mLocalAuthSource;

    @Mock
    private RemoteAuthSource mRemoteAuthSource;

    private AuthRepository mAuthRepository;

    private static final String URL = "URL";

    private static final String TOKEN = "TOKEN";

    private static final String KEY = "KEY";

    private static final String DEF_VALUE = "DEF VALUE";

    private static final String RESULT = "RESULT";

    private static final Authorization AUTHORIZATION_INFO =
            new Authorization("Login", "pass");

    private static final AuthorizationAnswer ANSWER =
            new AuthorizationAnswer("authorized", "token");

    private static final Single<AuthorizationAnswer> AUTH_ANSWER = Single.just(ANSWER);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mAuthRepository = new AuthRepositoryImpl(mLocalAuthSource, mRemoteAuthSource);
    }

    @After
    public void tearDown() {
        mLocalAuthSource = null;
        mRemoteAuthSource = null;
        mAuthRepository = null;
    }

    @Test
    public void testCreateAuthRepo() {
        assertNotNull(mAuthRepository);
    }

    @Test
    public void testSaveUrl() {
        mAuthRepository.saveUrl(URL);

        verify(mLocalAuthSource).saveUrl(URL);
    }

    @Test
    public void testGetUrl() {
        when(mLocalAuthSource.getUrl()).thenReturn(URL);

        String url = mAuthRepository.getUrl();

        verify(mLocalAuthSource).getUrl();

        assertEquals(url, URL);
    }

    @Test
    public void testSaveIsConnectionSecured() {
        mAuthRepository.saveIsUserEnabledSecuredConnection(IS_SECURED);

        verify(mLocalAuthSource).saveIsUserEnabledSecuredConnection(IS_SECURED);
    }

    @Test
    public void testGetIsSecuredConnection() {
        when(mLocalAuthSource.getIsConnectionSecured()).thenReturn(IS_SECURED);

        boolean is_secured = mAuthRepository.getIsConnectionSecured();

        verify(mLocalAuthSource).getIsConnectionSecured();

        assertEquals(IS_SECURED, is_secured);
    }

    @Test
    public void testSaveToken() {
        mAuthRepository.saveToken(TOKEN);

        verify(mLocalAuthSource).saveToken(TOKEN);
    }

    @Test
    public void testGetToken() {
        when(mLocalAuthSource.getToken()).thenReturn(TOKEN);

        String token = mAuthRepository.getToken();

        verify(mLocalAuthSource).getToken();

        assertEquals(token, TOKEN);
    }

    @Test
    public void testDeleteToken() {
        doNothing().when(mLocalAuthSource).deleteToken();

        mAuthRepository.deleteToken();

        verify(mLocalAuthSource).deleteToken();
    }

    @Test
    public void testGetSummaryByKey() {
        when(mLocalAuthSource.getSummaryByKey(KEY, DEF_VALUE)).thenReturn(RESULT);

        String result = mAuthRepository.getSummaryByKey(KEY, DEF_VALUE);

        assertEquals(result, RESULT);
    }

    @Test
    public void testAuth() {
        when(mRemoteAuthSource.authUser(AUTHORIZATION_INFO)).thenReturn(AUTH_ANSWER);

        doNothing().when(mLocalAuthSource).saveToken(any(String.class));

        doNothing().when(mLocalAuthSource).deleteToken();

        mAuthRepository.authUser(AUTHORIZATION_INFO)
                .test()
                .assertNoErrors()
                .assertValue(ANSWER)
                .assertComplete();

        verify(mLocalAuthSource).saveToken(any(String.class));
        verify(mRemoteAuthSource).destroyApiService();
    }

    @Test
    public void  testDeleteApiService() {
        doNothing().when(mRemoteAuthSource).destroyApiService();

        mAuthRepository.destroyApiService();

        verify(mRemoteAuthSource).destroyApiService();
    }
}
