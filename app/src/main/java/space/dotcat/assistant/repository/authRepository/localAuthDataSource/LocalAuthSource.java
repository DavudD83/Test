package space.dotcat.assistant.repository.authRepository.localAuthDataSource;


import android.support.annotation.NonNull;

public interface LocalAuthSource {

    void saveUrl(@NonNull String url);

    String getUrl();

    void saveToken(@NonNull String token);

    String getToken();

    void deleteToken();

    String getSummaryByKey(@NonNull String key, @NonNull String defaultValue);
}
