package space.dotcat.assistant.screen.settings;


import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.assistant.BuildConfig;
import space.dotcat.assistant.R;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.utils.AddressUtils;
import space.dotcat.assistant.utils.UrlUtils;

public class SettingsPresenter implements BasePresenter {

    private final SettingsViewContract mViewContract;

    private final AuthRepository mAuthRepository;

    private boolean mIsConnectionSecured;

    public SettingsPresenter(SettingsViewContract viewContract, AuthRepository authRepository) {
        mViewContract = viewContract;

        mAuthRepository = authRepository;

        mIsConnectionSecured = getIsConnectionSecured();
    }

    public void init() {
        mViewContract.showSummary();
    }

    public void saveNewUrl(@NonNull String base_url) {
        mAuthRepository.saveUrl(base_url);
    }

    public void saveNewHost(@NonNull String host) {
        mAuthRepository.saveHostValue(host);
    }

    public void saveNewPort(@NonNull String port) {
        mAuthRepository.savePortValue(port);
    }

    public void saveNewStreamingApi(@NonNull String streaming_url ) {
        mAuthRepository.saveStreamingUrl(streaming_url);
    }

    public boolean validateUrl(@NonNull String url) {
        if (!UrlUtils.isValidURL(url)) {
            mViewContract.showUrlError();

            return false;
        } else {
            return true;
        }
    }

    public void recreateApi() {
        mAuthRepository.destroyApiService();
    }

    public void updateParticularPreferenceSummary(String key, String summary) {
        mViewContract.updateParticularSummary(key, summary);
    }

    public String getPreferenceSummary(@NonNull String key, @NonNull String default_value) {
        return mAuthRepository.getSummaryByKey(key, default_value);
    }

    public boolean getIsConnectionSecured() {
        return mAuthRepository.getIsConnectionSecured();
    }

    public String createBaseUrl(boolean isSecured, String host, String port) {
        return AddressUtils.createBaseAddress(isSecured, host, port);
    }

    public String createStreamingUrl(boolean isSecured, String host, String port) {
        return AddressUtils.createSteamingAddress(isSecured, host, port);
    }

    public void updateAddresses() {
        String string_host = mAuthRepository.getHostValue();

        String string_port = mAuthRepository.getPortValue();

        String base_url = createBaseUrl(mIsConnectionSecured, string_host, string_port);

        String streaming_url = createStreamingUrl(mIsConnectionSecured, string_host, string_port);

        saveNewUrl(base_url);
        saveNewStreamingApi(streaming_url);

        recreateApi();
    }

    @Override
    public void unsubscribe() {

    }
}
