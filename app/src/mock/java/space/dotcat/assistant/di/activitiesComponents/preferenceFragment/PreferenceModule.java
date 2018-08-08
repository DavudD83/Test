package space.dotcat.assistant.di.activitiesComponents.preferenceFragment;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.screen.settings.SettingsPresenter;
import space.dotcat.assistant.screen.settings.SettingsViewContract;

@Module
public class PreferenceModule {

    private SettingsViewContract mViewContract;

    public PreferenceModule(SettingsViewContract viewContract) {
        mViewContract = viewContract;
    }

    @Provides
    @ActivityScope
    SettingsPresenter provideSettingsPresenter(AuthRepository authRepository) {
        return new SettingsPresenter(mViewContract, authRepository);
    }
}
