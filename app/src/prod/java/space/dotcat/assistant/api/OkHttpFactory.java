package space.dotcat.assistant.api;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;

public class OkHttpFactory {

    private volatile OkHttpClient sOkHttpClient;

    private SharedPreferences mSharedPreferences;

    public OkHttpFactory(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public OkHttpClient provideClient(){
        OkHttpClient client = sOkHttpClient;
        if(client == null){
            synchronized (OkHttpFactory.class){
                client = sOkHttpClient;
                if(client == null) {
                    client = sOkHttpClient = buildClient();
                }
            }
        }

        return client;
    }

    @NonNull
    private OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(mSharedPreferences))
                .build();
    }

    public void recreate() {
        sOkHttpClient = null;

        sOkHttpClient = provideClient();
    }

    public void deleteClient() {
        sOkHttpClient = null;
    }
}
