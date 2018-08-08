package space.dotcat.assistant.screen.setup;

public interface SetupViewContract {

    void showAuthActivity();

    void showHostError();

    void showPortError();

    void showEmptyHost();

    void showEmptyPort();

    void showExistingHost(String host);

    void showExistingPort(String port);

    void showExistingIsConnectionSecured(boolean is_connection_secured);
}
