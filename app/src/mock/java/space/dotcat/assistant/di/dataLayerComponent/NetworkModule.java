package space.dotcat.assistant.di.dataLayerComponent;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.api.BasicErrorParser;
import space.dotcat.assistant.api.ErrorParser;
import space.dotcat.assistant.api.OkHttpFactory;
import space.dotcat.assistant.api.RequestMatcher;

@Module
public class NetworkModule {

    @Provides
    @DataScope
    RequestMatcher provideRequestMatcher(Context context, SharedPreferences sharedPreferences) {
        return new RequestMatcher(context, sharedPreferences);
    }

    @Provides
    @DataScope
    ErrorParser provideErrorParser() {
        return new BasicErrorParser();
    }

    @Provides
    @DataScope
    OkHttpFactory provideOkHttpFactory(SharedPreferences sharedPreferences, RequestMatcher requestMatcher) {
        return new OkHttpFactory(sharedPreferences, requestMatcher);
    }

    @Provides
    @DataScope
    ApiFactory provideApiFactory(SharedPreferences sharedPreferences, OkHttpFactory okHttpFactory,
                                 ErrorParser errorParser) {
        return new ApiFactory(sharedPreferences, okHttpFactory, errorParser);
    }
}
