package space.dotcat.assistant.repository.authRepository;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.AuthRepository;
import space.dotcat.assistant.repository.DefaultAuthRepository;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AuthRepositoryTest {

    private AuthRepository mAuthRepository;

    private final static AuthorizationAnswer ANSWER =
            new AuthorizationAnswer("", "TOKEN");

    private final static Url URL = new Url("https://api.ks-cube.tk/");

    @Before
    public void init() throws Exception {
        mAuthRepository = new DefaultAuthRepository();
    }

    @After
    public void clear() throws Exception {
        Realm.getDefaultInstance().executeTransaction(transaction -> {
            transaction.delete(AuthorizationAnswer.class);
            transaction.delete(Url.class);
        });
    }

    @Test
    public void testSaveToken() throws Exception {
        mAuthRepository.saveAuthorizationAnswer(ANSWER);

        String token = mAuthRepository.token();

        assertEquals(ANSWER.getToken(), token);
    }

    @Test
    public void testTokenNotSaved() throws Exception {
        String token = mAuthRepository.token();

        assertEquals("", token);
    }

    @Test
    public void testDeleteToken() throws Exception {
        mAuthRepository.saveAuthorizationAnswer(ANSWER);

        mAuthRepository.deleteToken();

        String token = mAuthRepository.token();

        assertEquals("", token);
    }

    @Test
    public void testSaveUrl() throws Exception {
        mAuthRepository.saveUrl(URL);

        String url = mAuthRepository.url();

        assertEquals(url, URL.getUrl());
    }
}
