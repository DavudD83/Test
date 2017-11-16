package space.dotcat.assistant.repository;


import android.support.annotation.NonNull;

import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Url;

public interface AuthRepository {

    void saveAuthorizationAnswer(@NonNull AuthorizationAnswer answer);

    String token();

    void deleteToken();

    void saveUrl(@NonNull Url url);

    @NonNull
    String url();
}
