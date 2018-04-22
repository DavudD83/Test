package space.dotcat.assistant.di.activitiesComponents.roomDetails;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import space.dotcat.assistant.di.activitiesComponents.ActivityScope;
import space.dotcat.assistant.repository.thingsRepository.ThingRepository;
import space.dotcat.assistant.screen.general.LoadingDialog;
import space.dotcat.assistant.screen.general.LoadingView;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsPresenter;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsViewContract;
import space.dotcat.assistant.screen.settings.PreferenceFragment;

@Module
public class RoomDetailsModule {

    private RoomDetailsViewContract mRoomDetailsViewContract;

    private RoomDetailsAdapter.OnItemChange mOnItemChange;

    private FragmentManager mFragmentManager;

    public RoomDetailsModule(RoomDetailsViewContract roomDetailsViewContract,
                             RoomDetailsAdapter.OnItemChange onItemChange,
                             FragmentManager fragmentManager) {
        mRoomDetailsViewContract = roomDetailsViewContract;

        mOnItemChange = onItemChange;

        mFragmentManager = fragmentManager;
    }

    @Provides
    @ActivityScope
    RoomDetailsPresenter provideRoomDetailsPresenter(ThingRepository thingRepository) {
        return new RoomDetailsPresenter(mRoomDetailsViewContract, thingRepository);
    }

    @Provides
    @ActivityScope
    RoomDetailsAdapter provideRoomDetailsAdapter() {
        return new RoomDetailsAdapter(new ArrayList<>(), mOnItemChange);
    }

    @Provides
    @ActivityScope
    LoadingView provideLoadingView() {
        return LoadingDialog.view(mFragmentManager);
    }
}
