package space.dotcat.assistant.repository.authRepository;


import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;

public interface AuthRepository {

    /**
     * Save particular ulr into a phone's disk memory
     *
     * @param url - address of server which you want to connect with
     */

    void saveUrl(@NonNull String url);

    /**
     * Get url value from the disk. Returns default value if url was not been saved
     *
     * @return address of server which you are now connected with
     */

    String getUrl();

    /**
     * Save particular token into a phone's disk memory
     *
     * @param token - user token
     */

    void saveToken(@NonNull String token);

    /**
     * Get token from the disk. Returns default value if token was not saved
     *
     * @return user token
     */

    String getToken();

    /**
     * Delete user token from a phone's memory disk
     *
     */

    void deleteToken();

    /**
     * Get preference summary by given key, return default value as well if summary was not saved
     *
     * @param key - preference key
     * @param defaultValue - default value for summary
     * @return - summary for preference
     */

    String getSummaryByKey(@NonNull String key, @NonNull String defaultValue);

    /**
     * Try to authorize as a user
     *
     * @param authorizationInfo - object that contains login and password
     * @return - single with auth answer from the server
     */

    Single<AuthorizationAnswer> authUser(@NonNull Authorization authorizationInfo);

    /**
     * Destroy api service instance in order to create it again when it be needed
     *
     */

    void destroyApiService();
}
