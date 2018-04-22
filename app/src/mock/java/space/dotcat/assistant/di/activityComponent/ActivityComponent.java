package space.dotcat.assistant.di.activitiesComponents;

import dagger.Subcomponent;
import space.dotcat.assistant.screen.auth.AuthActivity;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsActivity;
import space.dotcat.assistant.screen.roomList.RoomsActivity;
import space.dotcat.assistant.screen.settings.PreferenceFragment;

@Subcomponent
@ActivityScope
public interface ActivityComponent {

    void inject(AuthActivity authActivity);

    void inject(RoomsActivity roomsActivity);

    void inject(RoomDetailsActivity roomDetailsActivity);

    void inject(PreferenceFragment preferenceFragment);
}
