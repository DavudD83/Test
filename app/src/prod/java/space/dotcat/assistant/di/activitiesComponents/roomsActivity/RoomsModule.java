package space.dotcat.assistant.di.activitiesComponents.roomsActivity;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.screen.general.LoadingDialog;
import space.dotcat.assistant.screen.general.LoadingView;
import space.dotcat.assistant.screen.roomList.RoomsAdapter;
import space.dotcat.assistant.screen.roomList.RoomsPresenter;
import space.dotcat.assistant.screen.roomList.RoomsViewContract;
import space.dotcat.assistant.service.MessageServiceHandler;
import space.dotcat.assistant.service.ServiceHandler;

@Module
public class RoomsModule {

    private RoomsViewContract mRoomsViewContract;

    private RoomsAdapter.OnItemClickListener<Room> mOnItemClick;

    private FragmentManager mFragmentManager;

    public RoomsModule(RoomsViewContract roomsViewContract, RoomsAdapter.OnItemClickListener<Room> onItemClick,
                       FragmentManager fragmentManager) {
        mRoomsViewContract = roomsViewContract;

        mOnItemClick = onItemClick;

        mFragmentManager = fragmentManager;
    }

    @Provides
    @ActivityScope
    RoomsPresenter provideRoomsPresenter(RoomRepository roomsRepository, ServiceHandler messageServiceHandler) {
        return new RoomsPresenter(mRoomsViewContract, roomsRepository, messageServiceHandler);
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
