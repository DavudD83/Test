package space.dotcat.assistant.di.activitiesComponents.roomDetails;

import dagger.Subcomponent;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsActivity;

@Subcomponent(modules = {RoomDetailsModule.class})
@ActivityScope
public interface RoomDetailsActivityComponent {

    void inject(RoomDetailsActivity roomDetailsActivity);
}
