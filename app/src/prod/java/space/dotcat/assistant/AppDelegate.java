package space.dotcat.assistant;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppDelegate extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;

        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);
    }

    public static Context getContext() {
        return sContext;
    }
}
