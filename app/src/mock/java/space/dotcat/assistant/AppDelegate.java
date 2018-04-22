package space.dotcat.assistant;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import space.dotcat.assistant.di.appComponent.AppComponent;
import space.dotcat.assistant.di.appComponent.AppModule;
import space.dotcat.assistant.di.appComponent.DaggerAppComponent;
import space.dotcat.assistant.di.appComponent.DatabaseModule;
import space.dotcat.assistant.di.appComponent.SharedPreferencesModule;
import space.dotcat.assistant.di.dataLayerComponent.DataLayerComponent;
import space.dotcat.assistant.di.dataLayerComponent.NetworkModule;
import space.dotcat.assistant.di.dataLayerComponent.RepoModule;

public class AppDelegate extends Application {

    private static AppDelegate sInstance;

    private AppComponent mAppComponent;
    private DataLayerComponent mDataLayerComponent;

    public static AppDelegate getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        initDependencyGraph();

        if(LeakCanary.isInAnalyzerProcess(this)){
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        LeakCanary.install(this);
    }

    private void initDependencyGraph() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .databaseModule(new DatabaseModule())
                .sharedPreferencesModule(new SharedPreferencesModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public DataLayerComponent plusDataLayerComponent() {
        if(mDataLayerComponent == null)
            mDataLayerComponent = mAppComponent.plusDataLayerComponent(new NetworkModule(),
                    new RepoModule());

        return mDataLayerComponent;
    }

    public void releaseAppComponent() {
        mAppComponent = null;
    }

    public void releaseDataLayerComponent() {
        mDataLayerComponent = null;
    }
}
