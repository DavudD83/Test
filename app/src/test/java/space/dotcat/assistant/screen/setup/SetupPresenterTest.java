package space.dotcat.assistant.screen.setup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import space.dotcat.assistant.repository.authRepository.AuthRepository;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SetupPresenterTest {

    private static final String HOST_VALUE = "example.com";

    private static final String PORT_VALUE = "10800";

    private SetupPresenter mSetupPresenter;

    @Mock
    private SetupViewContract mSetupViewContract;

    @Mock
    private AuthRepository mAuthRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mSetupPresenter = new SetupPresenter(mSetupViewContract, mAuthRepository);
    }

    @After
    public void clear() {
        mSetupPresenter = null;
    }

    @Test
    public void testPresenterCreated() {
        assertNotNull(mSetupPresenter);
    }

    @Test
    public void testSetupCompleted() {
        when(mAuthRepository.isSetupCompleted()).thenReturn(true);

        mSetupPresenter.init();

        verify(mSetupViewContract).showAuthActivity();
    }

    @Test
    public void testSetupIsNotCompleted() {
        when(mAuthRepository.isSetupCompleted()).thenReturn(false);

        when(mAuthRepository.getHostValue()).thenReturn(HOST_VALUE);

        when(mAuthRepository.getPortValue()).thenReturn(PORT_VALUE);

        mSetupPresenter.init();

        verify(mSetupViewContract).showExistingHost(HOST_VALUE);
        verify(mSetupViewContract).showExistingPort(PORT_VALUE);
    }

    @Test
    public void testCompleteSetupWithEmptyPort() {
        mSetupPresenter.completeSetup(true, HOST_VALUE, "");

        verify(mSetupViewContract).showEmptyPort();
    }

    @Test
    public void testCompleteSetupWithEmptyHost() {
        mSetupPresenter.completeSetup(true, "", PORT_VALUE);

        verify(mSetupViewContract).showEmptyHost();
    }

    @Test
    public void testCompleteSetupWithEmptyHostAndPort() {
        mSetupPresenter.completeSetup(true, "", "");

        verify(mSetupViewContract).showEmptyHost();
    }

    @Test
    public void testCompleteSetupSuccessfully() {
        mSetupPresenter.completeSetup(true, HOST_VALUE, PORT_VALUE);

        verify(mAuthRepository).saveHostValue(HOST_VALUE);
        verify(mAuthRepository).savePortValue(PORT_VALUE);

        verify(mAuthRepository).saveSetupState(true);

        verify(mSetupViewContract).showAuthActivity();
    }
}
