package space.dotcat.assistant.di.activitiesComponents.roomsActivity;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.screen.general.LoadingDialog;
import space.dotcat.assistant.screen.general.LoadingView;
import space.dotcat.assistant.screen.roomList.RoomsAdapter;
import space.dotcat.assistant.screen.roomList.RoomsPresenter;
import space.dotcat.assistant.screen.roomList.RoomsViewContract;

@Module
public class RoomsModule {

    private RoomsViewContract mRoomsViewContract;

    private RoomsAdapter.OnItemClick mOnItemClick;

    private FragmentManager mFragmentManager;

    public RoomsModule(RoomsViewContract roomsViewContract, RoomsAdapter.OnItemClick onItemClick,
                       FragmentManager fragmentManager) {
        mRoomsViewContract = roomsViewContract;

        mOnItemClick = onItemClick;

        mFragmentManager = fragmentManager;
    }

    @Provides
    @ActivityScope
    RoomsPresenter provideRoomsPresenter(RoomRepository roomsRepository) {
        return new RoomsPresenter(mRoomsViewContract, roomsRepository);
    }

    @Provides
    @ActivityScope
    RoomsAdapter provideRoomsAdapter() {
        return new RoomsAdapter(new ArrayList<>(), mOnItemClick);
    }

    @Provides
    @ActivityScope
    LoadingView provideLoadingView() {
        return LoadingDialog.view(mFragmentManager);
    }
}
