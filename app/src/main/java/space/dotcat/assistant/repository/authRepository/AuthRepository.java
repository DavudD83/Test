package space.dotcat.assistant.repository.authRepository;


import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;

public interface AuthRepository {

    /**
     * Save setup state into a phone's disk memory
     *
     * @param isCompleted state of setup process
     */

    void saveSetupState(boolean isCompleted);


    /**
     * Get state of setup process
     *
     * @return boolean that indicates state of setup process
     */

    boolean isSetupCompleted();


    /**
     * Save host value
     *
     * @param host string that represents host value of address
     */

    void saveHostValue(String host);

    /**
     * Get host value
     *
     * @return string that represents host
     */
    String getHostValue();


    /**
     * Save port value
     *
     * @param port string that represents port of address
     */
    void savePortValue(String port);

    /**
     * Get port value
     *
     * @return string that represents port
     */

    String getPortValue();

    /**
     * Save boolean value that represents is user enabled secured connection or not
     *
     * @param isSecured value that shows whether user enabled secured connection or not
     */

    void saveIsUserEnabledSecuredConnection(boolean isSecured);

    /**
     * Get info that show whether user enabled secured connection or not
     *
     * @return boolean value
     */

    boolean getIsConnectionSecured();

    /**
     * Save particular ulr into a phone's disk memory
     *
     * @param url address of server api which you want to connect with
     */

    void saveUrl(@NonNull String url);

    /**
     * Get api url value from the disk. Returns default value if url was not been saved
     *
     * @return address of server which you are now connected with
     */

    String getUrl();

    /**
     * Save particular streaming url into a phone's disk memory
     *
     * @param ws_url address of streaming api you want to connect with
     */

    void saveStreamingUrl(String ws_url);

    /**
     *  Get streaming url from disk memory
     *
     * @return string that represents url streaming address
     */

    String getStreamingUrl();

    /**
     * Save particular token into a phone's disk memory
     *
     * @param token user token
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
     * @param key preference key
     * @param defaultValue default value for summary
     * @return summary for preference
     */

    String getSummaryByKey(@NonNull String key, @NonNull String defaultValue);

    /**
     * Try to authorize as a user
     *
     * @param authorizationInfo object that contains login and password
     * @return single with auth answer from the server
     */

    Single<AuthorizationAnswer> authUser(@NonNull Authorization authorizationInfo);

    /**
     * Destroy api service instance in order to create it again when it be needed
     *
     */

    void destroyApiService();


    /**
     * Get an information about message service service state
     * 
     * @return boolean that represents state of service
     */

    @Deprecated
    boolean isMessageServiceStarted();

    /**
     * Save message service state
     *
     * @param state state of message service
     */

    @Deprecated
    void saveMessageServiceState(boolean state);

}
