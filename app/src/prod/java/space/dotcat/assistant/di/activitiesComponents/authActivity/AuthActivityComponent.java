package space.dotcat.assistant.di.activitiesComponents.authActivity;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.screen.auth.AuthActivity;

@Subcomponent(modules = AuthActivityModule.class)
@ActivityScope
public interface AuthActivityComponent {

    void inject(AuthActivity authActivity);
}
