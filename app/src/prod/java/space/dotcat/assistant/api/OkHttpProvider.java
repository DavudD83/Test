package space.dotcat.assistant.api;


import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;

public class OkHttpProvider {

    private static volatile OkHttpClient sOkHttpClient;

    private OkHttpProvider() {
    }

    public static OkHttpClient provideClient(){
        OkHttpClient client = sOkHttpClient;
        if(client == null){
            synchronized (OkHttpProvider.class){
                client = sOkHttpClient;
                if(client == null){
                    client = sOkHttpClient = buildClient();
                }
            }
        }

        return client;
    }

    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(AuthenticationInterceptor.create())
                .build();
    }

    public static void recreate() {
        sOkHttpClient = null;

        sOkHttpClient = provideClient();
    }

    public static void deleteClient() {
        sOkHttpClient = null;
    }
}
