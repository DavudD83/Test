package space.dotcat.assistant.screen.auth;


import space.dotcat.assistant.screen.general.LoadingView;

public interface AuthView extends LoadingView {

    void showRoomList();

    void showLoginError();

    void showPasswordError();

    void showAuthError();
}
