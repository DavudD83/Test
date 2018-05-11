package space.dotcat.assistant.repository.authRepository.localAuthDataSource;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import space.dotcat.assistant.BuildConfig;

public class LocalAuthSourceImpl implements LocalAuthSource {

    private SharedPreferences mSharedPreferences;

    private static final String URL_DEFAULT_VALUE = "https://api.ks-cube.tk/api/rest/v1";

    private static final String TOKEN_DEFAULT_VALUE = "";

    private static final String HOST_DEFAULT_VALUE = "";

    private static final String PORT_DEFAULT_VALUE = "11800";

    private static final String STREAMING_URL_DEFAULT_VALUE = "ws://api.ks-cube.tk/streaming/api/v1";

    private static final boolean SETUP_PROCESS_DEFAULT_VALUE = false;

    private static final boolean MESSAGE_SERVICE_STATE_DEFAULT_VALUE = false;

    public LocalAuthSourceImpl(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void saveSetupState(boolean isCompleted) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putBoolean(BuildConfig.PROCESS_STATE_KEY, isCompleted);

        editor.apply();
    }

    @Override
    public boolean isStateCompleted() {
        return mSharedPreferences.getBoolean(BuildConfig.PROCESS_STATE_KEY, SETUP_PROCESS_DEFAULT_VALUE);
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

    @Override
    public void saveHostValue(String host) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(BuildConfig.HOST_KEY, host);

        editor.apply();
    }

    @Override
    public String getHost() {
        return mSharedPreferences.getString(BuildConfig.HOST_KEY, HOST_DEFAULT_VALUE);
    }

    @Override
    public void savePortValue(String port) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(BuildConfig.PORT_KEY, PORT_DEFAULT_VALUE);

        editor.apply();
    }

    @Override
    public String getPort() {
        return mSharedPreferences.getString(BuildConfig.PORT_KEY, PORT_DEFAULT_VALUE);
    }

    @Override
    public void saveStreamingUrl(String ws_url) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(BuildConfig.STREAMING_URL_KEY, ws_url);

        editor.apply();
    }

    @Override
    public String getStreamingUrl() {
        return mSharedPreferences.getString(BuildConfig.STREAMING_URL_KEY, STREAMING_URL_DEFAULT_VALUE);
    }

    @Override
    public boolean isMessageServiceStarted() {
        return mSharedPreferences.getBoolean(BuildConfig.MESSAGE_SERVICE_STATE_KEY, MESSAGE_SERVICE_STATE_DEFAULT_VALUE);
    }

    @Override
    public void saveMessageServiceState(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putBoolean(BuildConfig.MESSAGE_SERVICE_STATE_KEY, state);

        editor.apply();
    }
}
