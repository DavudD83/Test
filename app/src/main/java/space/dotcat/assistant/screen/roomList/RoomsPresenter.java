package space.dotcat.assistant.screen.roomList;


import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.RepositoryProvider;

import ru.arturvasilov.rxloader.LifecycleHandler;
import space.dotcat.assistant.screen.general.BasePresenter;

public class RoomsPresenter implements BasePresenter {

    private final LifecycleHandler mLifecycleHandler;

    private final RoomsView mRoomsView;

    private CompositeSubscription mCompositeSubscription;

    public RoomsPresenter(@NonNull LifecycleHandler lifecycleHandler,
                          @NonNull RoomsView roomsView) {
        mLifecycleHandler = lifecycleHandler;
        mRoomsView = roomsView;
        mCompositeSubscription = new CompositeSubscription();
    }

    public void init() {
        Subscription subscription = RepositoryProvider.provideApiRepository()
                .rooms()
                .doOnSubscribe(mRoomsView::showLoading)
                .doOnTerminate(mRoomsView::hideLoading)
                .compose(mLifecycleHandler.load(R.id.room_request))
                .subscribe(mRoomsView::showRooms, mRoomsView::showError);

        mCompositeSubscription.add(subscription);
    }

    public void reloadData() {
        mCompositeSubscription.clear();

        Subscription subscription = RepositoryProvider.provideApiRepository()
                .rooms()
                .compose(mLifecycleHandler.reload(R.id.room_request))
                .subscribe(mRoomsView::showRooms, mRoomsView::showError);

        mCompositeSubscription.add(subscription);
    }

    public void onItemClick(@NonNull Room room) {
        mRoomsView.showRoomDetail(room);
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.clear();
    }
}
