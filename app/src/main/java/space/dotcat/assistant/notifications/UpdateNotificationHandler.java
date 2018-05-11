package space.dotcat.assistant.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class UpdateNotificationHandler extends BaseNotificationHandler {

    public UpdateNotificationHandler(Context context, NotificationManager notificationManager) {
        super(context, notificationManager);
    }

    @Override
    public void sendNotification() {

    }

    @Override
    public void cancelNotification() {

    }

    @Override
    protected PendingIntent getPendingIntent() {
       //TODO
        return null;
    }
}
