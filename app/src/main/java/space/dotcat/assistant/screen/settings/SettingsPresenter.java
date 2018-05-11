package space.dotcat.assistant.screen.settings;


import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.utils.UrlUtils;

public class SettingsPresenter {

    private final SettingsViewContract mViewContract;

    private final AuthRepository mAuthRepository;

    public SettingsPresenter(SettingsViewContract viewContract, AuthRepository authRepository) {
        mViewContract = viewContract;

        mAuthRepository = authRepository;
    }

    public void init() {
        mViewContract.showSummary();
    }

    public void saveNewUrl(@NonNull String saved_url) {
        mAuthRepository.saveUrl(saved_url);
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
}
