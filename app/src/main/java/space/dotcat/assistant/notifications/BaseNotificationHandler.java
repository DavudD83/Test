package space.dotcat.assistant.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

public abstract class BaseNotificationHandler {

    protected Context mContext;

    protected NotificationManager mNotificationManager;

    public BaseNotificationHandler(Context context, NotificationManager notificationManager) {
        mContext = context;

        mNotificationManager = notificationManager;
    }

    public abstract void sendNotification();

    public abstract void cancelNotification();

    protected abstract PendingIntent getPendingIntent();
}
