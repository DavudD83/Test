package space.dotcat.assistant.screen.auth;


import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.utils.TextUtils;
import space.dotcat.assistant.utils.UrlUtils;

public class AuthPresenter implements BasePresenter {

    private final AuthViewContract mAuthViewContract;

    private AuthRepository mAuthRepository;

    private CompositeDisposable mCompositeDisposable;

    public AuthPresenter(@NonNull AuthViewContract authView, @NonNull AuthRepository authRepository) {
        mAuthViewContract = authView;

        mAuthRepository = authRepository;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void init() {
        String token = mAuthRepository.getToken();

        if (!TextUtils.isEmpty(token)) {
            mAuthViewContract.showRoomList();
        }

        String url = mAuthRepository.getUrl();

        if (!TextUtils.isEmpty(url)) {
            mAuthViewContract.showExistingUrl(url);
        }
    }

    public void tryLogin(@NonNull String url, @NonNull String login, @NonNull String password) {
        if (TextUtils.isEmpty(url)) {
            mAuthViewContract.showUrlEmptyError();
        } else if (!UrlUtils.isValidURL(url)) {
            mAuthViewContract.showUrlNotCorrectError();
        } else if (TextUtils.isEmpty(login)) {
            mAuthViewContract.showLoginError();
        } else if (TextUtils.isEmpty(password)) {
            mAuthViewContract.showPasswordError();
        } else {
            mAuthRepository.saveUrl(url);

            mAuthRepository.destroyApiService();

            Authorization auth = new Authorization(login, password);

            Disposable authorizationAnswer = mAuthRepository
                    .authUser(auth)
                    .doOnSubscribe(disposable1 -> mAuthViewContract.showLoading())
                    .doAfterTerminate(mAuthViewContract::hideLoading)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            answer -> mAuthViewContract.showRoomList(),

                            mAuthViewContract::showAuthError);

            mCompositeDisposable.add(authorizationAnswer);
        }
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
