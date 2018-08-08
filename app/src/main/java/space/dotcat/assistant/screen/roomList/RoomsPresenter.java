package space.dotcat.assistant.screen.roomList;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.roomsRepository.RoomRepository;
import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.screen.general.BaseRxPresenter;
import space.dotcat.assistant.service.ServiceHandler;

public class RoomsPresenter extends BaseRxPresenter {

    private final RoomsViewContract mRoomsViewContract;

    private final RoomRepository mRoomRepository;

    private final ServiceHandler mMessageServiceHandler;

    public RoomsPresenter(@NonNull RoomsViewContract roomsViewContract, @NonNull RoomRepository roomRepository,
                          ServiceHandler messageServiceHandler) {
        mRoomsViewContract = roomsViewContract;

        mRoomRepository = roomRepository;

        mMessageServiceHandler = messageServiceHandler;
    }

    public void init() {
        Disposable rooms = mRoomRepository
                .getRooms()
                .doOnSubscribe(disposable -> mRoomsViewContract.showLoading())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        roomList -> {
                            if (roomList.isEmpty()) {
                                reloadRooms();

                                mRoomsViewContract.hideLoading();

                                mRoomsViewContract.showEmptyRoomsMessage();
                            }

                            else {
                                mRoomsViewContract.hideLoading();

                                mRoomsViewContract.showRooms(roomList);
                            }
                        },

                        throwable -> {
                            mRoomsViewContract.hideLoading();

                            mRoomsViewContract.showError(throwable);
                        });

        mCompositeDisposable.add(rooms);

        startSyncService();
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

    private void startSyncService() {
        mMessageServiceHandler.startService();
    }

    public void onItemClick(@NonNull Room room) {
        mRoomsViewContract.showRoomDetail(room);
    }
}
