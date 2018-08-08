package space.dotcat.assistant.di.activitiesComponents.authActivity;

import android.support.v4.app.FragmentManager;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.screen.auth.AuthPresenter;
import space.dotcat.assistant.screen.auth.AuthViewContract;
import space.dotcat.assistant.screen.general.LoadingDialog;
import space.dotcat.assistant.screen.general.LoadingView;

@Module
public class AuthActivityModule {

    private AuthViewContract mAuthViewContract;

    private FragmentManager mFragmentManager;

    public AuthActivityModule(AuthViewContract authViewContract, FragmentManager fragmentManager) {
        mAuthViewContract = authViewContract;

        mFragmentManager = fragmentManager;
    }

    @Provides
    @ActivityScope
    AuthPresenter provideAuthPresenter(AuthRepository authRepository) {
        return new AuthPresenter(mAuthViewContract, authRepository);
    }

    @Provides
    @ActivityScope
    LoadingView provideLoadingView() {
        return LoadingDialog.view(mFragmentManager);
    }
}
