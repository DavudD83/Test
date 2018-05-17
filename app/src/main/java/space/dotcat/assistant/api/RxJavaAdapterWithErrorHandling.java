package space.dotcat.assistant.api;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import space.dotcat.assistant.content.ApiError;

public class RxJavaAdapterWithErrorHandling extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory mFactory;

    private final ErrorParser mErrorParser;

    public RxJavaAdapterWithErrorHandling(ErrorParser errorParser) {
        mFactory = RxJava2CallAdapterFactory.create();

        mErrorParser = errorParser;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxAdapterWrapper<>(retrofit, mFactory.get(returnType, annotations, retrofit));
    }

    public class RxAdapterWrapper<R> implements CallAdapter<R, Single<?>> {

        private final Retrofit mRetrofit;

        private final CallAdapter<R, ?> mWrapped;

        public RxAdapterWrapper(Retrofit retrofit, CallAdapter<R, ?> wrapped) {
            mRetrofit = retrofit;
            mWrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return mWrapped.responseType();
        }

        @Override
        public Single<R> adapt(Call<R> call) {

            return ((Single<R>) mWrapped.adapt(call)).onErrorResumeNext(throwable -> {
                Throwable t = (Throwable) throwable;

                ApiError apiError = new ApiError();

                if (throwable instanceof HttpException) {
                    HttpException exception = (HttpException) t;

                    apiError = mErrorParser.parseError(mRetrofit, exception.response());

                    if (apiError == null) {
                        apiError = new ApiError();

                        apiError.setUserMessage("Server is not available");
                    }
                }

                if (throwable instanceof UnknownHostException) {
                    apiError.setUserMessage("Unknown url");
                }

                if (throwable instanceof SocketTimeoutException) {
                    apiError.setUserMessage("Connection failed");
                }

                t = apiError;

                return Single.error(t);
            });
        }
    }
}
