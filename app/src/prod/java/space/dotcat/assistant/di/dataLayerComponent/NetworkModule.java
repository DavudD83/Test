package space.dotcat.assistant.di.dataLayerComponent;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.api.BasicErrorParser;
import space.dotcat.assistant.api.ErrorParser;
import space.dotcat.assistant.api.OkHttpFactory;

@Module
public class NetworkModule {

    @Provides
    @DataScope
    ErrorParser provideErrorParser() {
        return new BasicErrorParser();
    }

    @Provides
    @DataScope
    OkHttpFactory provideOkHttpFactory(SharedPreferences sharedPreferences) {
        return new OkHttpFactory(sharedPreferences);
    }

    @Provides
    @DataScope
    ApiFactory provideApiFactory(SharedPreferences sharedPreferences, OkHttpFactory okHttpFactory,
                                 ErrorParser errorParser) {
        return new ApiFactory(sharedPreferences, okHttpFactory, errorParser);
    }
}
