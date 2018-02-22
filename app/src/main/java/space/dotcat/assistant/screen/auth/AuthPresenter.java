package space.dotcat.assistant.screen.auth;


import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.utils.TextUtils;
import space.dotcat.assistant.utils.UrlUtils;

public class AuthPresenter implements BasePresenter {

    private final AuthView mAuthView;

    private CompositeDisposable mCompositeDisposable;

    public AuthPresenter(@NonNull AuthView authView) {
        mAuthView = authView;

        mCompositeDisposable = new CompositeDisposable();
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

    public void tryLogin(@NonNull String url,@NonNull String login,@NonNull String password){
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

            Disposable authorizationAnswer = RepositoryProvider.provideApiRepository()
                    .auth(auth)
                    .doOnSubscribe(disposable1 -> mAuthView.showLoading())
                    .doAfterTerminate(mAuthView::hideLoading)
                    .subscribe(answer ->  mAuthView.showRoomList(),
                            mAuthView::showAuthError);

            mCompositeDisposable.add(authorizationAnswer);
        }
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
