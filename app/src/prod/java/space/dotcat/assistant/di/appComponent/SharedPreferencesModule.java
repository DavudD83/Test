package space.dotcat.assistant.di.appComponent;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.R;

@Module
public class SharedPreferencesModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getResources().getString(R.string.preferences_name),
                Context.MODE_PRIVATE);
    }
}
