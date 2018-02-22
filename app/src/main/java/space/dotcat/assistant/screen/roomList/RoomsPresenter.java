package space.dotcat.assistant.screen.roomList;


import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.general.BasePresenter;

public class RoomsPresenter implements BasePresenter {

    private final RoomsView mRoomsView;

    private CompositeDisposable mCompositeDisposable;

    public RoomsPresenter(@NonNull RoomsView roomsView) {
        mRoomsView = roomsView;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void init() {
        Disposable rooms = RepositoryProvider.provideApiRepository()
                .rooms()
                .doOnSubscribe(disposable -> mRoomsView.showLoading())
                .doOnTerminate(mRoomsView::hideLoading)
                .subscribe(mRoomsView::showRooms, mRoomsView::showError);

        mCompositeDisposable.add(rooms);
    }

    public void reloadData() {
        mCompositeDisposable.clear();

        Disposable subscription = RepositoryProvider.provideApiRepository()
                .rooms()
                .subscribe(mRoomsView::showRooms, mRoomsView::showError);

        mCompositeDisposable.add(subscription);
    }

    public void onItemClick(@NonNull Room room) {
        mRoomsView.showRoomDetail(room);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
