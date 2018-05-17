package space.dotcat.assistant.di.serviceComponent;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.utils.WiFiListener;

@Module
public class WiFiListenerModule {

    @Provides
    @ServiceScope
    LiveData<Boolean> provideWiFiListener(Context context) {
        return new WiFiListener(context);
    }
}
