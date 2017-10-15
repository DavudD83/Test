package space.dotcat.assistant.screen.auth;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import ru.arturvasilov.rxloader.LifecycleHandler;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;

public class AuthPresenter {

    private final AuthView mAuthView;

    private final LifecycleHandler mLifecycleHandler;

    public AuthPresenter(@NonNull AuthView authView,
                         @NonNull LifecycleHandler lifecycleHandler) {
        mAuthView = authView;
        mLifecycleHandler = lifecycleHandler;
    }

    public void init(){
        String token = RepositoryProvider.provideApiRepository().token();

        if(!TextUtils.isEmpty(token))
            mAuthView.showRoomList();
    }

    public void tryLogin(String url, String login, String password){
        if(TextUtils.isEmpty(url)){
            mAuthView.showUrlEmptyError();
        } else if(!url.startsWith("http://") && (!url.startsWith("https://"))) {
            mAuthView.showUrlNotCorrectError();
        }
        else if(TextUtils.isEmpty(login)){
            mAuthView.showLoginError();
        } else if(TextUtils.isEmpty(password)){
            mAuthView.showPasswordError();
        } else {
            Url urlForRequest = new Url(url);

            RepositoryProvider.provideApiRepository().saveUrl(urlForRequest);

            Authorization auth = new Authorization(login, password);

            RepositoryProvider.provideApiRepository()
                    .auth(auth)
                    .doOnSubscribe(mAuthView::showLoading)
                    .doOnTerminate(mAuthView::hideLoading)
                    .compose(mLifecycleHandler.reload(R.id.auth_request))
                    .subscribe(authorizationAnswer ->  mAuthView.showRoomList(),
                            mAuthView::showAuthError);
        }
    }
}
