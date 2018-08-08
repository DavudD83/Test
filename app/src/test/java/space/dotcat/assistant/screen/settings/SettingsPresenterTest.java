package space.dotcat.assistant.screen.settings;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import space.dotcat.assistant.BuildConfig;
import space.dotcat.assistant.base.BasePresenterTest;
import space.dotcat.assistant.repository.authRepository.AuthRepository;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsPresenterTest extends BasePresenterTest<SettingsPresenter> {

    @Mock
    private SettingsViewContract mSettingsViewContract;

    @Mock
    private AuthRepository mAuthRepository;

    private final static String VALID_URL = "https://109.23.32.32/";

    private final static String INVALID_URL = "url/";

    private final static String KEY = "KEY";

    private final static String SUMMARY = "SUMMARY";

    private final static String HOST = "HOST";

    private final static String PORT = "PORT";

    @Override
    protected SettingsPresenter createPresenterForTesting() {
        return new SettingsPresenter(mSettingsViewContract, mAuthRepository);
    }

    @Test
    public void testPresenterSuccessfullyCreated() {
        assertNotNull(mPresenter);
    }

    @Test
    public void testPresenterInit() {
        mPresenter.init();

        Mockito.verify(mSettingsViewContract).showSummary();
    }

    @Test
    public void testUrlSuccessfullySaved() {
        mPresenter.saveNewUrl(VALID_URL);

        Mockito.verify(mAuthRepository).saveUrl(VALID_URL);
    }

    @Test
    public void testUpdateParticularPreferenceSummary() {
        mPresenter.updateParticularPreferenceSummary(KEY, SUMMARY);

        Mockito.verify(mSettingsViewContract).updateParticularSummary(KEY, SUMMARY);
    }

    @Test
    public void testCheckInvalidUrl() {
        mPresenter.validateUrl(INVALID_URL);

        Mockito.verify(mSettingsViewContract).showUrlError();
    }

    @Test
    public void testRecreateApi() {
        mPresenter.recreateApi();

        Mockito.verify(mAuthRepository).destroyApiService();
    }

    @Test
    public void testGetPreferenceSummary() {
        String def_value = "DEF_VALUE";
        String result = "RESULT";

        when(mAuthRepository.getSummaryByKey(KEY, def_value)).thenReturn(result);

        String value = mPresenter.getPreferenceSummary(KEY, def_value);

        assertEquals(value, result);

        Mockito.verify(mAuthRepository).getSummaryByKey(KEY, def_value);
    }

    @Test
    public void getIsConnectionSecured() {
        boolean is_secured = false;

        when(mAuthRepository.getIsConnectionSecured()).thenReturn(is_secured);

        boolean actual = mPresenter.getIsConnectionSecured();

        verify(mAuthRepository, times(2)).getIsConnectionSecured();

        assertEquals(is_secured, actual);
    }

    @Test
    public void testUpdateAddresses() {
        mPresenter.updateAddresses();

        verify(mAuthRepository).getHostValue();
        verify(mAuthRepository).getPortValue();

        verify(mAuthRepository).saveStreamingUrl(anyString());
        verify(mAuthRepository).saveUrl(anyString());
        verify(mAuthRepository).destroyApiService();
    }

    @Test
    public void testScenario() {
        mPresenter.init();

        Mockito.verify(mSettingsViewContract).showSummary();
        verify(mAuthRepository).getIsConnectionSecured();

        mPresenter.saveNewHost(HOST);
        verify(mAuthRepository).saveHostValue(HOST);

        mPresenter.updateAddresses();
        verify(mAuthRepository).getPortValue();
        verify(mAuthRepository).getHostValue();
        verify(mAuthRepository).saveStreamingUrl(anyString());
        verify(mAuthRepository).saveUrl(anyString());

        mPresenter.updateParticularPreferenceSummary(KEY, SUMMARY);
        Mockito.verify(mSettingsViewContract).updateParticularSummary(KEY, SUMMARY);
    }
}
