package space.dotcat.assistant.di.appComponent;

import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.notifications.BaseNotificationHandler;
import space.dotcat.assistant.notifications.NotificationHandler;

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    NotificationHandler provideNotificationHandler(Context context, NotificationManager notificationManager) {
        return new BaseNotificationHandler(context, notificationManager);
    }
}
