package space.dotcat.assistant.service;

/**
 * Base contract for classes who are going to manage service lifecycle
 */
public interface ServiceHandler {

    /**
     * Start your service
     */

    void startService();

    /**
     * Stop your service
     */

    void stopService();
}
