package space.dotcat.assistant.api;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import space.dotcat.assistant.repository.RepositoryProvider;

class AuthenticationInterceptor implements Interceptor {

    private final String mToken;

    private AuthenticationInterceptor() {
        mToken = RepositoryProvider.provideAuthRepository().token();
    }

    static Interceptor create() {
        return new AuthenticationInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if(TextUtils.isEmpty(mToken)){
            return chain.proceed(chain.request());
        }

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", mToken)
                .build();

        return chain.proceed(request);
    }
}
