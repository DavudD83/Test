package space.dotcat.assistant.api;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import space.dotcat.assistant.content.ApiError;

class RxJavaAdapterWithErrorHandling extends CallAdapter.Factory {

    private final RxJavaCallAdapterFactory mFactory;

    RxJavaAdapterWithErrorHandling() {
        mFactory = RxJavaCallAdapterFactory.create();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxAdapterWrapper(retrofit, mFactory.get(returnType, annotations, retrofit));
    }

    public class RxAdapterWrapper implements CallAdapter<Observable<?>>{

        private final Retrofit mRetrofit;

        private final CallAdapter<?> mWrapped;

        public RxAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped) {
            mRetrofit = retrofit;
            mWrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return mWrapped.responseType();
        }

        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            return ((Observable)mWrapped.adapt(call)).onErrorResumeNext(throwable -> {
                Throwable t = (Throwable) throwable;

                ApiError apiError = new ApiError();

                if(throwable instanceof HttpException){
                    HttpException exception = (HttpException) t;

                    apiError = ErrorParser.parseError(exception.response());

                    if(apiError == null) {
                        apiError = new ApiError();

                        apiError.setUserMessage("Server is not available");
                    }
                }

                if(throwable instanceof UnknownHostException){
                    apiError.setUserMessage("Unknown url");
                }

                if(throwable instanceof SocketTimeoutException){
                    apiError.setUserMessage("Connection failed");
                }

                t = apiError;

                return Observable.error(t);
            });
        }
    }
}
