package space.dotcat.assistant.screen.auth;


import space.dotcat.assistant.screen.general.LoadingView;

public interface AuthView extends LoadingView {

    void showRoomList();

    void showUrlEmptyError();

    void showUrlNotCorrectError();

    void showLoginError();

    void showPasswordError();

    void showAuthError(Throwable t);
}
