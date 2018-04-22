package space.dotcat.assistant.repository.authRepository;


import android.support.annotation.NonNull;

import io.reactivex.Single;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;
import space.dotcat.assistant.repository.authRepository.remoteDataSource.RemoteAuthSource;

public class AuthRepositoryImpl implements AuthRepository {

    private LocalAuthSource mLocalAuthSource;

    private RemoteAuthSource mRemoteAuthSource;

    public AuthRepositoryImpl(LocalAuthSource authRepository, RemoteAuthSource remoteAuthSource) {
        mLocalAuthSource = authRepository;

        mRemoteAuthSource = remoteAuthSource;
    }

    @Override
    public void saveUrl(@NonNull String url) {
        mLocalAuthSource.saveUrl(url);
    }

    @Override
    public String getUrl() {
        return mLocalAuthSource.getUrl();
    }

    @Override
    public void saveToken(@NonNull String token) {
        mLocalAuthSource.saveToken(token);
    }

    @Override
    public String getToken() {
        return mLocalAuthSource.getToken();
    }

    @Override
    public void deleteToken() {
        mLocalAuthSource.deleteToken();
    }

    @Override
    public String getSummaryByKey(@NonNull String key, @NonNull String defaultValue) {
        return mLocalAuthSource.getSummaryByKey(key, defaultValue);
    }

    @Override
    public Single<AuthorizationAnswer> authUser(@NonNull Authorization authorizationInfo) {
        return mRemoteAuthSource.authUser(authorizationInfo)
                .flatMap(answer -> {
                    String token = answer.getToken();

                    mLocalAuthSource.saveToken(token);

                    mRemoteAuthSource.destroyApiService();

                    return Single.just(answer);
                });
    }

    @Override
    public void destroyApiService() {
        mRemoteAuthSource.destroyApiService();
    }
}
