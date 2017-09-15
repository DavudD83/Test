package space.dotcat.assistant.api;


import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import space.dotcat.assistant.BuildConfig;
import space.dotcat.assistant.content.RealmString;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Type;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.internal.IOException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public final class ApiFactory {

    private static volatile ApiService sService;

    private static volatile OkHttpClient sClient;

    private static Type token = new TypeToken<RealmList<RealmString>>(){}.getType();
    private static Gson gson =  new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .registerTypeAdapter(token, new TypeAdapter<RealmList<RealmString>>() {

                @Override
                public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                    // Ignore
                }

                @Override
                public RealmList<RealmString> read(JsonReader in) throws IOException, java.io.IOException {
                    RealmList<RealmString> list = new RealmList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(new RealmString(in.nextString()));
                    }
                    in.endArray();
                    return list;
                }
            })
            .create();

    private ApiFactory() {
    }

    @NonNull
    public static ApiService getApiService() {
        ApiService service = sService;
        if(service == null) {
            synchronized (ApiFactory.class) {
                service = sService;
                if(service == null) {
                    service = sService = buildRetrofit().create(ApiService.class);
                }
            }
        }
        return service;
    }

    @NonNull
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.Base_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    @NonNull
    private static OkHttpClient buildClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(AuthenticationInterceptor.create())
                .build();
    }


    private static OkHttpClient getClient(){
        OkHttpClient client = sClient;

        if(client == null){
            synchronized (ApiFactory.class){
                client = sClient;
                if(client == null)
                    client = sClient = buildClient();
            }
        }

        return client;
    }

    public static void recreate(){

        sClient = null;

        sClient = getClient();
        sService = buildRetrofit().create(ApiService.class);
    }
}
