package space.dotcat.assistant.screen.setup;

import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.utils.AddressUtils;
import space.dotcat.assistant.utils.TextUtils;

public class SetupPresenter implements BasePresenter {

    private SetupViewContract mSetupViewContract;

    private AuthRepository mAuthRepository;

    public SetupPresenter(SetupViewContract setupViewContract, AuthRepository authRepository) {
        mSetupViewContract = setupViewContract;

        mAuthRepository = authRepository;
    }

    public void init() {
        if (mAuthRepository.isSetupCompleted()) {
            mSetupViewContract.showAuthActivity();
        }

        String host = mAuthRepository.getHostValue();

        if (!TextUtils.isEmpty(host)) {
            mSetupViewContract.showExistingHost(host);
        }

        String port = mAuthRepository.getPortValue();

        if (!TextUtils.isEmpty(port)) {
            mSetupViewContract.showExistingPort(port);
        }

        boolean is_connection_secured = mAuthRepository.getIsConnectionSecured();

        mSetupViewContract.showExistingIsConnectionSecured(is_connection_secured);
    }

    public void completeSetup(boolean isSecured, String host, String port) {
        if (TextUtils.isEmpty(host)) {
            mSetupViewContract.showEmptyHost();
        } else if (TextUtils.isEmpty(port)) {
            mSetupViewContract.showEmptyPort();
        } else {
            mAuthRepository.savePortValue(port);
            mAuthRepository.saveHostValue(host);
            mAuthRepository.saveIsUserEnabledSecuredConnection(isSecured);

            String base_url = AddressUtils.createBaseAddress(isSecured, host, port);

            String streaming_url = AddressUtils.createSteamingAddress(isSecured, host, port);

            mAuthRepository.saveUrl(base_url);
            mAuthRepository.saveStreamingUrl(streaming_url);
            mAuthRepository.saveSetupState(true);

            mSetupViewContract.showAuthActivity();
        }
    }

    @Override
    public void unsubscribe() {

    }
}
