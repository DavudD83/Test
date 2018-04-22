package space.dotcat.assistant.screen.settings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SettingsPresenterTest {

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    @Mock
    private SettingsViewContract mSettingsViewContract;

    @Mock
    private AuthRepository mAuthRepository;

    private SettingsPresenter mSettingsPresenter;

    private final static String VALID_URL = "https://109.23.32.32/";

    private final static String INVALID_URL = "url/";

    private final static String KEY = "KEY";

    private final static String SUMMARY = "SUMMARY";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mSettingsPresenter = new SettingsPresenter(mSettingsViewContract, mAuthRepository);
    }

    @After
    public void clear() {
        mSettingsViewContract = null;
        mAuthRepository = null;
        mSettingsPresenter = null;
    }

    @Test
    public void testPresenterSuccessfullyCreated() {
        assertNotNull(mSettingsPresenter);
    }

    @Test
    public void testPresenterInit() {
        mSettingsPresenter.init();

        Mockito.verify(mSettingsViewContract).showSummary();
    }

    @Test
    public void testUrlSuccessfullySaved() {
        mSettingsPresenter.saveNewUrl(VALID_URL);

        Mockito.verify(mAuthRepository).saveUrl(VALID_URL);
    }

    @Test
    public void testUpdateParticularPreferenceSummary() {
        mSettingsPresenter.updateParticularPreferenceSummary(KEY, SUMMARY);

        Mockito.verify(mSettingsViewContract).updateParticularSummary(KEY, SUMMARY);
    }

    @Test
    public void testCheckInvalidUrl() {
        mSettingsPresenter.validateUrl(INVALID_URL);

        Mockito.verify(mSettingsViewContract).showUrlError();
    }

    @Test
    public void testCheckValidUrl() {
        Boolean value = mSettingsPresenter.validateUrl(VALID_URL);

        assertTrue(value);

        Mockito.verifyNoMoreInteractions(mAuthRepository);
    }

    @Test
    public void testRecreateApi() {
        mSettingsPresenter.recreateApi();

        Mockito.verify(mAuthRepository).destroyApiService();
    }

    @Test
    public void testGetPreferenceSummary() {
        String def_value = "DEF_VALUE";
        String result = "RESULT";

        when(mAuthRepository.getSummaryByKey(KEY, def_value)).thenReturn(result);

        String value = mSettingsPresenter.getPreferenceSummary(KEY, def_value);

        assertEquals(value, result);

        Mockito.verify(mAuthRepository).getSummaryByKey(KEY, def_value);
    }

    @Test
    public void testScenario() {
        mSettingsPresenter.init();
        Mockito.verify(mSettingsViewContract).showSummary();

        mSettingsPresenter.validateUrl(INVALID_URL);
        Mockito.verify(mSettingsViewContract).showUrlError();

        mSettingsPresenter.validateUrl(VALID_URL);
        Mockito.verifyZeroInteractions(mSettingsViewContract);

        mSettingsPresenter.saveNewUrl(VALID_URL);
        Mockito.verify(mAuthRepository).saveUrl(VALID_URL);

        mSettingsPresenter.recreateApi();
        Mockito.verify(mAuthRepository).destroyApiService();

        mSettingsPresenter.updateParticularPreferenceSummary(KEY, SUMMARY);
        Mockito.verify(mSettingsViewContract).updateParticularSummary(KEY, SUMMARY);
    }
}
