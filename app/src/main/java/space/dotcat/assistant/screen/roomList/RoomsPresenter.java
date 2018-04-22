package space.dotcat.assistant.screen.roomList;


import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.screen.general.BasePresenter;

public class RoomsPresenter implements BasePresenter {

    private final RoomsViewContract mRoomsViewContract;

    private final RoomRepository mRoomRepository;

    private CompositeDisposable mCompositeDisposable;

    public RoomsPresenter(@NonNull RoomsViewContract roomsViewContract, @NonNull RoomRepository roomRepository) {
        mRoomsViewContract = roomsViewContract;

        mRoomRepository = roomRepository;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void init() {
        Disposable rooms = mRoomRepository
                .getRooms()
                .doOnSubscribe(disposable -> mRoomsViewContract.showLoading())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        roomList -> {
                            mRoomsViewContract.hideLoading();

                            if (roomList.isEmpty())
                                mRoomsViewContract.showEmptyRoomsMessage();

                            else
                                mRoomsViewContract.showRooms(roomList);
                        },

                        throwable -> {
                            mRoomsViewContract.hideLoading();

                            mRoomsViewContract.showError(throwable);
                        });

        mCompositeDisposable.add(rooms);
    }

    public void reloadRooms() {
        Disposable subscription = mRoomRepository
                .refreshRooms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        rooms -> {},

                        mRoomsViewContract::showError);

        mCompositeDisposable.add(subscription);
    }

    public void onItemClick(@NonNull Room room) {
        mRoomsViewContract.showRoomDetail(room);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
