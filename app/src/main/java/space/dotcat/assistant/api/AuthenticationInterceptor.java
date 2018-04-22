package space.dotcat.assistant.api;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import space.dotcat.assistant.BuildConfig;

public class AuthenticationInterceptor implements Interceptor {

    private final String mToken;

    public AuthenticationInterceptor(SharedPreferences sharedPreferences) {
        mToken = sharedPreferences.getString(BuildConfig.TOKEN_KEY, "");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if(TextUtils.isEmpty(mToken)) {
            return chain.proceed(chain.request());
        }

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", mToken)
                .build();

        return chain.proceed(request);
    }
}
