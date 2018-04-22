package space.dotcat.assistant.repository.authRepository.localAuthDataSource;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import space.dotcat.assistant.BuildConfig;

public class LocalAuthSourceImpl implements LocalAuthSource {

    private SharedPreferences mSharedPreferences;

    private static final String URL_DEFAULT_VALUE = "https://api.ks-cube.tk/";

    private static final String TOKEN_DEFAULT_VALUE = "";

    public LocalAuthSourceImpl(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void saveUrl(@NonNull String url) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(BuildConfig.URL_KEY, url);

        editor.apply();
    }

    @Override
    public String getUrl() {
        return mSharedPreferences.getString(BuildConfig.URL_KEY, URL_DEFAULT_VALUE);
    }

    @Override
    public void saveToken(@NonNull String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(BuildConfig.TOKEN_KEY, token);

        editor.apply();
    }

    @Override
    public String getToken() {
        return mSharedPreferences.getString(BuildConfig.TOKEN_KEY, TOKEN_DEFAULT_VALUE);
    }

    @Override
    public void deleteToken() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.remove(BuildConfig.TOKEN_KEY);

        editor.apply();
    }

    @Override
    public String getSummaryByKey(@NonNull String key, @NonNull String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }
}
