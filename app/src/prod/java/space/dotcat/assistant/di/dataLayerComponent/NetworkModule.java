package space.dotcat.assistant.di.dataLayerComponent;

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
import space.dotcat.assistant.webSocket.WebSocketService;

@Module
public class NetworkModule {

    @Provides
    @DataScope
    ErrorParser provideErrorParser() {
        return new BasicErrorParser();
    }

    @Provides
    @DataScope
    OkHttpClient provideCleanOkHttpClient() {
        return new OkHttpClient.Builder()
                .pingInterval(1, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @DataScope
    Gson provideGson() {
        return new Gson();
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
