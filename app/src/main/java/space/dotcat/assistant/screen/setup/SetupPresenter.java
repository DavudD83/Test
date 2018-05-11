package space.dotcat.assistant.screen.setup;

import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.utils.AddressUtils;
import space.dotcat.assistant.utils.TextUtils;

public class SetupPresenter {

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
    }

    public void completeSetup(boolean isSecured, String host, String port) {
        if (TextUtils.isEmpty(host)) {
            mSetupViewContract.showEmptyHost();
        } else if (TextUtils.isEmpty(port)) {
            mSetupViewContract.showEmptyPort();
        } else {
            mAuthRepository.saveHostValue(host);
            mAuthRepository.savePortValue(port);

            String base_url = AddressUtils.createBaseAddress(isSecured, host, port);

            String streaming_url = AddressUtils.createSteamingAddress(isSecured, host, port);

            mAuthRepository.saveUrl(base_url);
            mAuthRepository.saveStreamingUrl(streaming_url);
            mAuthRepository.saveSetupState(true);

            mSetupViewContract.showAuthActivity();
        }
    }
}
