package space.dotcat.assistant.di.activitiesComponents.setupActivity;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.screen.setup.SetupPresenter;
import space.dotcat.assistant.screen.setup.SetupViewContract;

@Module
public class SetupActivityModule {

    private SetupViewContract mSetupViewContract;

    public SetupActivityModule(SetupViewContract setupViewContract) {
        mSetupViewContract = setupViewContract;
    }

    @Provides
    @ActivityScope
    SetupPresenter provideSetupPresenter(AuthRepository authRepository) {
        return new SetupPresenter(mSetupViewContract, authRepository);
    }

}
