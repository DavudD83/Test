package space.dotcat.assistant.screen.settings;


import android.support.annotation.NonNull;

public interface SettingsViewContract {

    void showSummary();

    void updateParticularSummary(@NonNull String preference_key, @NonNull String summary);

    void showUrlError();
}
