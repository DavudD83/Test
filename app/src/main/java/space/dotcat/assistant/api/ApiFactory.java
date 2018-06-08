package space.dotcat.assistant.api;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;


import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import space.dotcat.assistant.BuildConfig;

public final class ApiFactory {

    private volatile ApiService mService;

    private SharedPreferences mSharedPreferences;

    private OkHttpFactory mOkHttpFactory;

    private Converter.Factory mConverterFactory;

    private CallAdapter.Factory mCallAdapterFactory;

    public ApiFactory(SharedPreferences sharedPreferences, OkHttpFactory okHttpFactory, Converter.Factory converterFactory,
                      CallAdapter.Factory callAdapterFactory) {
        mSharedPreferences = sharedPreferences;

        mOkHttpFactory = okHttpFactory;

        mConverterFactory = converterFactory;

        mCallAdapterFactory = callAdapterFactory;
    }

    @NonNull
    public ApiService getApiService() {
        ApiService service = mService;

        if(service == null) {
            synchronized (ApiFactory.class) {
                service = mService;

                if(service == null) {
                    service = mService = buildRetrofit().create(ApiService.class);
                }
            }
        }

        return service;
    }

    @NonNull
    private Retrofit buildRetrofit() {
        String base_url = mSharedPreferences.getString(BuildConfig.URL_KEY,
                BuildConfig.URL_DEFAULT_VALUE);

        OkHttpClient okHttpClient = mOkHttpFactory.provideClient();

        return new Retrofit.Builder()
                .baseUrl(base_url)
                .client(okHttpClient)
                .addConverterFactory(mConverterFactory)
                .addCallAdapterFactory(mCallAdapterFactory)
                .build();
    }

    public void recreate() {
        mOkHttpFactory.recreate();
        mService = buildRetrofit().create(ApiService.class);
    }

    public boolean isServiceDeleted() {
        return mService == null;
    }

    public void deleteInstance() {
        mOkHttpFactory.deleteClient();
        mService = null;
    }
}
