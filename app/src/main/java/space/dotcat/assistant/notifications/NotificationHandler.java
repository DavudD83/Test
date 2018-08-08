package space.dotcat.assistant.notifications;

import space.dotcat.assistant.content.Room;

/**
 * Interface that is responsible for dispatching and managing notifications objects
 */


public interface NotificationHandler {

    void sendEventNotification(String content, Room room);

    void cancelEventNotification();
}
