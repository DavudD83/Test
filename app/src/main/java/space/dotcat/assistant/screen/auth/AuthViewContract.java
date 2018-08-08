package space.dotcat.assistant.screen.auth;


import android.support.annotation.NonNull;

import space.dotcat.assistant.screen.general.LoadingView;

public interface AuthViewContract extends LoadingView {

    void showRoomList();

    void showExistingUrl(@NonNull String url);

    void showUrlEmptyError();

    void showUrlNotCorrectError();

    void showLoginError();

    void showPasswordError();

    void showAuthError(Throwable t);

    void showSetupActivity();
}
