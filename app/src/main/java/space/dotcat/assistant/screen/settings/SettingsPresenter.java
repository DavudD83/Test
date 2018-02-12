package space.dotcat.assistant.screen.settings;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.utils.UrlUtils;

public class SettingsPresenter {

    private final SettingsView mView;

    public SettingsPresenter(SettingsView view) {
        mView = view;
    }

    public void init() {
        mView.showSummary();
    }

    public void saveNewUrl(@NonNull String saved_url){
        Url new_url = new Url(saved_url);

        RepositoryProvider.provideAuthRepository().saveUrl(new_url);
    }

    public boolean validateUrl(@NonNull String url) {
        if(!UrlUtils.isValidURL(url)) {
            mView.showUrlError();

            return false;
        } else {
            return true;
        }
    }

    public void recreateApi(){
        ApiFactory.recreate();
    }

    public void updateParticularPreferenceSummary(String key, String summary) {
        mView.updateParticularSummary(key, summary);
    }

    public String getPreferenceSummary(@NonNull SharedPreferences sharedPreferences,
                                       @NonNull String key, @NonNull String default_value) {
        return sharedPreferences.getString(key, default_value);
    }
}
