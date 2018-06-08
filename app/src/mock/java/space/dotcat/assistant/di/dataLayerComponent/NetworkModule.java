package space.dotcat.assistant.di.dataLayerComponent;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.api.BasicErrorParser;
import space.dotcat.assistant.api.ErrorParser;
import space.dotcat.assistant.api.HierarchyTypeAdapterFactory;
import space.dotcat.assistant.api.OkHttpFactory;
import space.dotcat.assistant.api.RequestMatcher;
import space.dotcat.assistant.api.RxJavaAdapterWithErrorHandling;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Speaker;
import space.dotcat.assistant.content.Thing;

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
    TypeAdapterFactory provideHierarchyAdapterFactory() {
        return HierarchyTypeAdapterFactory.of(Thing.class, "type")
                .addSubtype(Lamp.class, "light")
                .addSubtype(DoorLock.class, "door_actuator")
                .addSubtype(DimmableLamp.class, "dimmable_light")
                .addSubtype(RGBLamp.class, "color_light")
                .addSubtype(Speaker.class);
    }

    @Provides
    @DataScope
    Gson provideGsonConvertor(TypeAdapterFactory hierarchyAdapterFactory) {
        return new GsonBuilder()
                .registerTypeAdapterFactory(hierarchyAdapterFactory)
                .create();
    }

    @Provides
    @DataScope
    Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @DataScope
    CallAdapter.Factory provideCallAdapterFactory(ErrorParser errorParser) {
        return new RxJavaAdapterWithErrorHandling(errorParser);
    }

    @Provides
    @DataScope
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .pingInterval(1, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS)
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
                                 Converter.Factory converterFactory, CallAdapter.Factory callAdapterFactory) {
        return new ApiFactory(sharedPreferences, okHttpFactory, converterFactory, callAdapterFactory);
    }
}
