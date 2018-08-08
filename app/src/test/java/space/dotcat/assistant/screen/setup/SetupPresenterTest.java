package space.dotcat.assistant.screen.setup;

import org.junit.Test;
import org.mockito.Mock;

import space.dotcat.assistant.base.BasePresenterTest;
import space.dotcat.assistant.repository.authRepository.AuthRepository;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetupPresenterTest extends BasePresenterTest<SetupPresenter> {

    private static final String HOST_VALUE = "example.com";

    private static final String PORT_VALUE = "10800";

    private static final boolean IS_SECURED = true;

    @Mock
    private SetupViewContract mSetupViewContract;

    @Mock
    private AuthRepository mAuthRepository;

    @Override
    protected SetupPresenter createPresenterForTesting() {
        return new SetupPresenter(mSetupViewContract, mAuthRepository);
    }

    @Test
    public void testPresenterCreated() {
        assertNotNull(mPresenter);
    }

    @Test
    public void testSetupCompleted() {
        when(mAuthRepository.isSetupCompleted()).thenReturn(true);

        mPresenter.init();

        verify(mSetupViewContract).showAuthActivity();
    }

    @Test
    public void testSetupIsNotCompleted() {
        when(mAuthRepository.isSetupCompleted()).thenReturn(false);

        when(mAuthRepository.getHostValue()).thenReturn(HOST_VALUE);

        when(mAuthRepository.getPortValue()).thenReturn(PORT_VALUE);

        when(mAuthRepository.getIsConnectionSecured()).thenReturn(IS_SECURED);

        mPresenter.init();

        verify(mSetupViewContract).showExistingHost(HOST_VALUE);
        verify(mSetupViewContract).showExistingPort(PORT_VALUE);
        verify(mSetupViewContract).showExistingIsConnectionSecured(IS_SECURED);
    }

    @Test
    public void testCompleteSetupWithEmptyPort() {
        mPresenter.completeSetup(true, HOST_VALUE, "");

        verify(mSetupViewContract).showEmptyPort();
    }

    @Test
    public void testCompleteSetupWithEmptyHost() {
        mPresenter.completeSetup(true, "", PORT_VALUE);

        verify(mSetupViewContract).showEmptyHost();
    }

    @Test
    public void testCompleteSetupWithEmptyHostAndPort() {
        mPresenter.completeSetup(true, "", "");

        verify(mSetupViewContract).showEmptyHost();
    }

    @Test
    public void testCompleteSetupSuccessfully() {
        mPresenter.completeSetup(IS_SECURED, HOST_VALUE, PORT_VALUE);

        verify(mAuthRepository).saveHostValue(HOST_VALUE);
        verify(mAuthRepository).savePortValue(PORT_VALUE);
        verify(mAuthRepository).saveIsUserEnabledSecuredConnection(IS_SECURED);

        verify(mAuthRepository).saveSetupState(true);

        verify(mSetupViewContract).showAuthActivity();
    }
}
