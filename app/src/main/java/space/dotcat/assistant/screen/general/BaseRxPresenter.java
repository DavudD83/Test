package space.dotcat.assistant.screen.general;

import io.reactivex.disposables.CompositeDisposable;

public class BaseRxPresenter implements BasePresenter {

    protected CompositeDisposable mCompositeDisposable;

    public BaseRxPresenter() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
