package space.dotcat.assistant.di.activitiesComponents.roomsActivity;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.screen.roomList.RoomsActivity;

@Subcomponent(modules = RoomsModule.class)
@ActivityScope
public interface RoomListActivityComponent {

    void inject(RoomsActivity roomsActivity);
}
