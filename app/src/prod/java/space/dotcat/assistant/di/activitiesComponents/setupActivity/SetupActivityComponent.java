package space.dotcat.assistant.di.activitiesComponents.setupActivity;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.screen.setup.SetupActivity;

@Subcomponent(modules = {SetupActivityModule.class})
@ActivityScope
public interface SetupActivityComponent {

    void inject(SetupActivity setupActivity);
}
