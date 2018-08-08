package space.dotcat.assistant.screen.settings;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import javax.inject.Inject;

import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.BuildConfig;
import space.dotcat.assistant.R;
import space.dotcat.assistant.di.activitiesComponents.preferenceFragment.PreferenceModule;

public class PreferenceFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener, SettingsViewContract {

    @Inject
    SettingsPresenter mSettingsPresenter;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_settings);

        AppDelegate.getInstance()
                .plusDataLayerComponent()
                .plusPreferencesComponent(new PreferenceModule(this))
                .inject(this);

        mSettingsPresenter.init();
    }

    @Override
    public void onStart() {
        super.onStart();

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.host_key))) {
            String string_host = mSettingsPresenter.getPreferenceSummary(key,
                    getString(R.string.default_host));

            mSettingsPresenter.saveNewHost(string_host);

            mSettingsPresenter.updateAddresses();

            mSettingsPresenter.updateParticularPreferenceSummary(key, string_host);
        } else if (key.equals(getString(R.string.port_key))) {
            String string_port = mSettingsPresenter.getPreferenceSummary(key, getString(R.string.default_port));

            mSettingsPresenter.saveNewPort(string_port);

            mSettingsPresenter.updateAddresses();

            mSettingsPresenter.updateParticularPreferenceSummary(key, string_port);
        }
    }

    @Override
    public void showSummary() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
            Preference preference = preferenceScreen.getPreference(i);

            String value = mSettingsPresenter.getPreferenceSummary(preference.getKey(), "");

            if (preference instanceof EditTextPreference) {
                preference.setSummary(value);
            }
        }
    }

    @Override
    public void updateParticularSummary(@NonNull String preference_key, @NonNull String summary) {
        Preference preference = findPreference(preference_key);

        preference.setSummary(summary);
    }

    @Override
    public void showUrlError() {
        Toast.makeText(getActivity(), getResources().getString(R.string.invalid_url),
                Toast.LENGTH_SHORT).show();
    }
}
