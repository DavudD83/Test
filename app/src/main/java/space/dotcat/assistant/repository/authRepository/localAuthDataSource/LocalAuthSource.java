package space.dotcat.assistant.repository.authRepository.localAuthDataSource;


import android.support.annotation.NonNull;

public interface LocalAuthSource {

    void saveSetupState(boolean isCompleted);

    boolean isStateCompleted();

    void saveUrl(@NonNull String url);

    String getUrl();

    void saveToken(@NonNull String token);

    String getToken();

    void deleteToken();

    String getSummaryByKey(@NonNull String key, @NonNull String defaultValue);

    void saveHostValue(String host);

    String getHost();

    void savePortValue(String port);

    String getPort();

    void saveStreamingUrl(String ws_url);

    void saveIsUserEnabledSecuredConnection(boolean isSecured);

    boolean getIsConnectionSecured();

    String getStreamingUrl();

    boolean isMessageServiceStarted();

    void saveMessageServiceState(boolean state);
}
