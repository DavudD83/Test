package space.dotcat.assistant.di.appComponent;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.R;

@Module
public class SharedPreferencesModule {

    private final String PREF_NAME_TEST = "com.dotcat.assistant.test_pref";

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME_TEST, Context.MODE_PRIVATE);
    }
}
