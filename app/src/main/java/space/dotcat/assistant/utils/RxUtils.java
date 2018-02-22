package space.dotcat.assistant.utils;


import android.support.annotation.NonNull;

import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxUtils {

    private RxUtils() {
    }

    //TODO Deal something with these similar methods, find a way how to create 1 method for all needs
    @NonNull
    public static <T> ObservableTransformer<T,T> makeObservableAsyncWithUiCallback() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true);
    }

    public static <T> SingleTransformer <T,T> makeSingleAsyncWithUiCallback() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
