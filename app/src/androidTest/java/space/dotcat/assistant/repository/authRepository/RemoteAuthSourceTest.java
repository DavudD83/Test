package space.dotcat.assistant.repository.authRepository;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.api.RequestMatcher;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.authRepository.remoteDataSource.RemoteAuthSource;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RemoteAuthSourceTest {

    private RemoteAuthSource mRemoteAuthSource;

    private LocalAuthSource mLocalAuthSource;

    private final static String TOKEN = "90ff4ba085545c1735ab6c29a916f9cb8c0b7222";

    private final static Authorization AUTHORIZATION_INFO = new Authorization("login", "pass");

    @Before
    public void init() {
        mRemoteAuthSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeRemoteAuthSource();

        mLocalAuthSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeLocalAuthSource();
    }

    @After
    public void clear() {
        mLocalAuthSource.deleteToken();
    }

    @Test
    public void testRemoteAuthSourceCreated() {
        assertNotNull(mRemoteAuthSource);
    }

    @Test
    public void testSuccessAuth() {
       AuthorizationAnswer authorizationAnswer =  mRemoteAuthSource.authUser(AUTHORIZATION_INFO)
               .blockingGet();

       assertEquals(TOKEN, authorizationAnswer.getToken());
    }

    @Test
    public void testErrorAuth() {
       mLocalAuthSource.saveToken(RequestMatcher.ERROR);

       mRemoteAuthSource.authUser(AUTHORIZATION_INFO)
               .test()
               .assertError(ApiError.class);
    }

    @Test
    public void testDestroyService() {
        mRemoteAuthSource.destroyApiService();

        assertTrue(mRemoteAuthSource.isApiDestroyed());
    }
}
