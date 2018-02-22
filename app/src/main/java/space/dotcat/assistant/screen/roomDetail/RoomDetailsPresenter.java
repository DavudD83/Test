package space.dotcat.assistant.screen.roomDetail;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import space.dotcat.assistant.content.ActionParams;
import space.dotcat.assistant.content.Body;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.general.BasePresenter;

public class RoomDetailsPresenter implements BasePresenter {

    private final RoomDetailsView mRoomDetailsView;

    private CompositeDisposable mCompositeDisposable;

    RoomDetailsPresenter(@NonNull RoomDetailsView roomDetailsView) {
        mRoomDetailsView = roomDetailsView;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void init(@NonNull String id) {
        Disposable things = RepositoryProvider.provideApiRepository()
                .things(id)
                .doOnSubscribe(disposable -> mRoomDetailsView.showLoading())
                .doOnTerminate(mRoomDetailsView::hideLoading)
                .subscribe(mRoomDetailsView::showThings, mRoomDetailsView::showError);

        mCompositeDisposable.add(things);
    }

    public void reloadData(@NonNull String id) {
        mCompositeDisposable.clear();

        Disposable things = RepositoryProvider.provideApiRepository()
                .things(id)
                .subscribe(mRoomDetailsView::showThings, mRoomDetailsView::showError);

        mCompositeDisposable.add(things);
    }

    public void onItemChange(@NonNull Thing thing) {
        ActionParams actionParams = new ActionParams();

        Body body = new Body("toggle", thing.getId(), actionParams);

        Message message = new Message(body);

        mCompositeDisposable.clear();

        Disposable responseMessage = RepositoryProvider.provideApiRepository()
                .action(message)
                .doOnSubscribe(disposable -> mRoomDetailsView.showLoading())
                .doAfterTerminate(mRoomDetailsView::hideLoading)
                .subscribe(message1 -> {}, mRoomDetailsView::showError);

        mCompositeDisposable.add(responseMessage);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
