package space.dotcat.assistant.di.activitiesComponents.preferenceFragment;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.screen.settings.PreferenceFragment;

@Subcomponent(modules = {PreferenceModule.class})
@ActivityScope
public interface PreferenceFragmentComponent {

    void inject(PreferenceFragment preferenceFragment);
}
