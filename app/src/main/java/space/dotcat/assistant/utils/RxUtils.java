package space.dotcat.assistant.utils;


import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxUtils {

    private RxUtils() {
    }

    @NonNull
    public static <T> ObservableTransformer<T,T> makeObservableAsyncWithUiCallback() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true);
    }

    public static <T> FlowableTransformer<T, T> makeFlowableAsyncWithUiCallback() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true);
    }

    public static <T> SingleTransformer <T,T> makeSingleAsyncWithUiCallback() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer makeCompletableAsyncWithUiCallback() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
