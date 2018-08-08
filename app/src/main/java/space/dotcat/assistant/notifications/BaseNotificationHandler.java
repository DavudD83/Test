package space.dotcat.assistant.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsActivity;


/**
 * Base implementation of notification handler which can send update events notifications
 */

public class BaseNotificationHandler implements NotificationHandler {

    private final static int EVENT_NOTIFICATION_ID = 10;

    private final static String CHANNEL_EVENT_ID = "UPDATE_NOTIFICATION_CHANNEL";

    private final static String EVENT_NOTIFICATION_TITLE = "Update notification";

    protected Context mContext;

    protected NotificationManager mNotificationManager;

    public BaseNotificationHandler(Context context, NotificationManager notificationManager) {
        mContext = context;

        mNotificationManager = notificationManager;
    }

    private NotificationCompat.Builder createBaseNotification(String description, String channelId, String channelName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setContentTitle(EVENT_NOTIFICATION_TITLE)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_notification)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create Notification channel via NotificationManager and eventually set this channel to notification
            //Otherwise this is lower version and there is no need to use channel and plain creation should be used
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        return builder;
    }

    private Notification createUpdateEventNotification(String description, String channelId,
                                                       String channelName, Room room) {
        NotificationCompat.Builder notificationBuilder = createBaseNotification(description, channelId, channelName);

        if (room != null) {
            Intent launchIntent = RoomDetailsActivity.getIntent(mContext, room);

            PendingIntent launchActivityPendingIntent = PendingIntent.getActivity(mContext, EVENT_NOTIFICATION_ID,
                    launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(launchActivityPendingIntent);
        }

        return notificationBuilder.build();
    }

    private void sendNotification(int notificationId, Notification notification) {
        mNotificationManager.notify(notificationId, notification);
    }

    private void cancelNotification(int notificationId) {
        mNotificationManager.cancel(notificationId);
    }

    @Override
    public void sendEventNotification(String content, Room room) {
        Notification notification = createUpdateEventNotification(content, CHANNEL_EVENT_ID,
                mContext.getString(R.string.update_event_channel_name), room);

        sendNotification(EVENT_NOTIFICATION_ID, notification);
    }

    @Override
    public void cancelEventNotification() {
        cancelNotification(EVENT_NOTIFICATION_ID);
    }
}
