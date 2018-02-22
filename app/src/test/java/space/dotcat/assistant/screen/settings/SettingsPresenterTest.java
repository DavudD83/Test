package space.dotcat.assistant.screen.settings;

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.testMock.MockAuthRepository;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class SettingsPresenterTest {

    private SettingsPresenter mSettingsPresenter;

    private SettingsView mSettingsView;

    private final static String VALID_URL = "https://109.23.32.32/";

    private final static String INVALID_URL = "url/";

    private final static String KEY = "KEY";
    private final static String SUMMARY = "SUMMARY";

    @Before
    public void init() {
        mSettingsView = Mockito.mock(SettingsView.class);

        mSettingsPresenter = new SettingsPresenter(mSettingsView);
    }

    @After
    public void clear() {
        mSettingsView = null;
        mSettingsPresenter = null;
    }

    @Test
    public void testPresenterSuccessfullyCreated() {
        assertNotNull(mSettingsPresenter);
    }

    @Test
    public void testPresenterInit() throws Exception {
        mSettingsPresenter.init();

        Mockito.verify(mSettingsView).showSummary();
    }

    @Test
    public void testUrlSuccessfullySaved() throws Exception {
        TestAuthRepo testAuthRepo = new TestAuthRepo();

        RepositoryProvider.setAuthRepository(testAuthRepo);

        mSettingsPresenter.saveNewUrl(VALID_URL);

        final String SAVED_URL = RepositoryProvider.provideAuthRepository().url();

        assertEquals(VALID_URL, SAVED_URL);
    }

    @Test
    public void testUpdateParticularPreferenceSummary() throws Exception {


        mSettingsPresenter.updateParticularPreferenceSummary(KEY, SUMMARY);

        Mockito.verify(mSettingsView).updateParticularSummary(KEY, SUMMARY);
    }

    @Test
    public void testCheckInvalidUrl() throws Exception {
        mSettingsPresenter.validateUrl(INVALID_URL);

        Mockito.verify(mSettingsView).showUrlError();
    }

    @Test
    public void testScenario() throws Exception {
        mSettingsPresenter.init();
        Mockito.verify(mSettingsView).showSummary();

        mSettingsPresenter.validateUrl(INVALID_URL);
        Mockito.verify(mSettingsView).showUrlError();

        mSettingsPresenter.validateUrl(VALID_URL);
        Mockito.verifyZeroInteractions(mSettingsView);

        mSettingsPresenter.updateParticularPreferenceSummary(KEY, SUMMARY);
        Mockito.verify(mSettingsView).updateParticularSummary(KEY, SUMMARY);
    }

    private class TestAuthRepo extends MockAuthRepository {

        private String mUrl;

        @NonNull
        @Override
        public String url() {
            return mUrl;
        }

        @Override
        public void saveUrl(Url url) {
            mUrl = url.getUrl();
        }
    }
}
