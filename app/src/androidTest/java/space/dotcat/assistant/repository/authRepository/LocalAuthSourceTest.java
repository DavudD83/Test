package space.dotcat.assistant.repository.authRepository;

import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.BuildConfig;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LocalAuthSourceTest {

    private static final String URL = "https://10.10.10.10";

    private static final String TOKEN = "TOKEN";

    private static final String KEY = "KEY";

    private static final String VALUE = "VALUE";

    private static final String DEF_VALUE = "DEF_VALUE";

    private SharedPreferences mSharedPreferences;

    private LocalAuthSource mLocalAuthSource;

    @Before
    public void init() {
        mSharedPreferences = AppDelegate.getInstance().getAppComponent().getFakeSharedPref();

        mLocalAuthSource = AppDelegate.getInstance().plusDataLayerComponent().getFakeLocalAuthSource();
    }

    @After
    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.clear();

        editor.commit();

        mSharedPreferences = null;
        mLocalAuthSource = null;
    }

    @Test
    public void testSaveUrl() {
        mLocalAuthSource.saveUrl(URL);

        String url = mLocalAuthSource.getUrl();

        assertEquals(URL, url);
    }

    @Test
    public void testGetUrlWhenUrlDidNotSavedBefore() {
        String url = mLocalAuthSource.getUrl();

        assertEquals(BuildConfig.URL_DEFAULT_VALUE, url);
    }

    @Test
    public void testSaveToken() {
        mLocalAuthSource.saveToken(TOKEN);

        assertEquals(mLocalAuthSource.getToken(), TOKEN);
    }

    @Test
    public void testGetTokenWhenTokenDidNotSavedBefore() {
        String token = mLocalAuthSource.getToken();

        assertEquals(BuildConfig.TOKEN_DEFAULT_VALUE, token);
    }

    @Test
    public void testGetSummaryByKeyWhenExist() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(KEY, VALUE);

        editor.commit();

        String summary = mLocalAuthSource.getSummaryByKey(KEY, VALUE);

        assertEquals(VALUE, summary);
    }

    @Test
    public void testGetSummaryByKeyWithDefault() {
        String summary = mLocalAuthSource.getSummaryByKey(KEY, DEF_VALUE);

        assertEquals(DEF_VALUE, summary);
    }
}
