package space.dotcat.assistant.di.dataLayerComponent;

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
import space.dotcat.assistant.api.RxJavaAdapterWithErrorHandling;
import space.dotcat.assistant.content.BinarySensor;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.PausablePlayer;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Speaker;
import space.dotcat.assistant.content.TemperatureSensor;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.TrackPlayer;
import space.dotcat.assistant.content.ValueSensor;

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
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Provides
    @DataScope
    TypeAdapterFactory provideHierarchyAdapterFactory() {
        return HierarchyTypeAdapterFactory.of(Thing.class, "type")
                .addSubtype(DoorLock.class, "door_actuator")
                .addSubtype(Lamp.class, "light")
                .addSubtype(ValueSensor.class, "value_sensor")
                .addSubtype(BinarySensor.class, "binary_sensor")
                .addSubtype(DimmableLamp.class, "dimmable_light")
                .addSubtype(ColorTemperatureLamp.class, "ct_light")
                .addSubtype(RGBLamp.class, "color_light")
                .addSubtype(TemperatureSensor.class, "temperature_sensor")
                .addSubtype(Player.class, "player")
                .addSubtype(PausablePlayer.class, "pausable_player")
                .addSubtype(TrackPlayer.class, "track_player")
                .addSubtype(Speaker.class, "speaker");
    }

    @Provides
    @DataScope
    Gson provideGson(TypeAdapterFactory hierarchyAdapterFactory) {
        return new GsonBuilder()
                .registerTypeAdapterFactory(hierarchyAdapterFactory)
                .serializeNulls()
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
    OkHttpFactory provideOkHttpFactory(SharedPreferences sharedPreferences) {
        return new OkHttpFactory(sharedPreferences);
    }

    @Provides
    @DataScope
    ApiFactory provideApiFactory(SharedPreferences sharedPreferences, OkHttpFactory okHttpFactory,
                                 Converter.Factory converterFactory,
                                 CallAdapter.Factory callAdapterFactory) {
        return new ApiFactory(sharedPreferences, okHttpFactory, converterFactory, callAdapterFactory);
    }
}
