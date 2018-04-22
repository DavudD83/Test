package space.dotcat.assistant.api;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;


import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import space.dotcat.assistant.BuildConfig;

public final class ApiFactory {

    private volatile ApiService mService;

    private SharedPreferences mSharedPreferences;

    private OkHttpFactory mOkHttpFactory;

    private ErrorParser mErrorParser;

    public ApiFactory(SharedPreferences sharedPreferences, OkHttpFactory okHttpFactory,
                      ErrorParser errorParser) {
        mSharedPreferences = sharedPreferences;

        mOkHttpFactory = okHttpFactory;

        mErrorParser = errorParser;
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
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new RxJavaAdapterWithErrorHandling(mErrorParser))
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
