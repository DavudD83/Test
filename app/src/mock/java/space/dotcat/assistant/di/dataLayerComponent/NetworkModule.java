package space.dotcat.assistant.di.dataLayerComponent;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
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
    Gson provideGsonConvertor() {
        return new Gson();
    }

    @Provides
    @DataScope
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
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
