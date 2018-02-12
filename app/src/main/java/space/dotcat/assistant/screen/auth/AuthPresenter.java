package space.dotcat.assistant.screen.auth;


import android.support.annotation.NonNull;


import ru.arturvasilov.rxloader.LifecycleHandler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.utils.TextUtils;
import space.dotcat.assistant.utils.UrlUtils;

public class AuthPresenter implements BasePresenter {

    private final AuthView mAuthView;

    private final LifecycleHandler mLifecycleHandler;

    private CompositeSubscription mCompositeSubscription;

    public AuthPresenter(@NonNull AuthView authView,
                         @NonNull LifecycleHandler lifecycleHandler) {
        mAuthView = authView;
        mLifecycleHandler = lifecycleHandler;
        mCompositeSubscription = new CompositeSubscription();
    }

    public void init(){
        String token = RepositoryProvider.provideAuthRepository().token();

        if(!TextUtils.isEmpty(token))
            mAuthView.showRoomList();

        String url = RepositoryProvider.provideAuthRepository().url();

        if(!TextUtils.isEmpty(url)){
            mAuthView.showExistingUrl(url);
        }
    }

    public void tryLogin(String url, String login, String password){
        if(TextUtils.isEmpty(url)){
            mAuthView.showUrlEmptyError();
        } else if(!UrlUtils.isValidURL(url)) {
            mAuthView.showUrlNotCorrectError();
        } else if(TextUtils.isEmpty(login)){
            mAuthView.showLoginError();
        } else if(TextUtils.isEmpty(password)){
            mAuthView.showPasswordError();
        } else {
            Url urlForRequest = new Url(url);

            RepositoryProvider.provideAuthRepository().saveUrl(urlForRequest);

            Authorization auth = new Authorization(login, password);

            Subscription subscription = RepositoryProvider.provideApiRepository()
                    .auth(auth)
                    .doOnSubscribe(mAuthView::showLoading)
                    .doOnTerminate(mAuthView::hideLoading)
                    .compose(mLifecycleHandler.reload(R.id.auth_request))
                    .subscribe(authorizationAnswer ->  mAuthView.showRoomList(),
                            mAuthView::showAuthError);

            mCompositeSubscription.add(subscription);
        }
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.clear();
    }
}
