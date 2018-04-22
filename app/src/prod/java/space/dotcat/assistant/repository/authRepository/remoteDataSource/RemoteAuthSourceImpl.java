package space.dotcat.assistant.repository.authRepository.remoteDataSource;


import android.support.annotation.NonNull;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.Single;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.api.ApiService;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;

public class RemoteAuthSourceImpl implements RemoteAuthSource {

    private ApiFactory mApiFactory;

    public RemoteAuthSourceImpl(ApiFactory apiFactory) {
        mApiFactory = apiFactory;
    }

    @Override
    public Single<AuthorizationAnswer> authUser(@NonNull Authorization authorizationInfo) {
        return mApiFactory.getApiService()
                .auth(authorizationInfo);
    }

    @Override
    public void destroyApiService() {
        mApiFactory.deleteInstance();
    }
}
