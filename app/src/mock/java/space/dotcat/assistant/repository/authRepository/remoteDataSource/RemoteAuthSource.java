package space.dotcat.assistant.repository.authRepository.remoteDataSource;


import android.support.annotation.NonNull;

import io.reactivex.Single;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;

public interface RemoteAuthSource {

    Single<AuthorizationAnswer> authUser(@NonNull Authorization authorizationInfo);

    void destroyApiService();

    boolean isApiDestroyed();
}
