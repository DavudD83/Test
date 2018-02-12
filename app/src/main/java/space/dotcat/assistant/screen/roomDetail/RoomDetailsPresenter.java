package space.dotcat.assistant.screen.roomDetail;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ActionParams;
import space.dotcat.assistant.content.Body;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.RepositoryProvider;

import ru.arturvasilov.rxloader.LifecycleHandler;
import space.dotcat.assistant.screen.general.BasePresenter;

public class RoomDetailsPresenter implements BasePresenter {

    private final LifecycleHandler mLifecycleHandler;

    private final RoomDetailsView mRoomDetailsView;

    private CompositeSubscription mCompositeSubscription;

    RoomDetailsPresenter(@NonNull LifecycleHandler lifecycleHandler,
                         @NonNull RoomDetailsView roomDetailsView) {
        mLifecycleHandler = lifecycleHandler;
        mRoomDetailsView = roomDetailsView;
        mCompositeSubscription = new CompositeSubscription();
    }

    public void init(@NonNull String id) {
        Subscription subscription = RepositoryProvider.provideApiRepository()
                .things(id)
                .doOnSubscribe(mRoomDetailsView::showLoading)
                .doOnTerminate(mRoomDetailsView::hideLoading)
                .compose(mLifecycleHandler.load(R.id.thing_request))
                .subscribe(mRoomDetailsView::showThings, mRoomDetailsView::showError);

        mCompositeSubscription.add(subscription);
    }

    public void reloadData(@NonNull String id) {
        mCompositeSubscription.clear();

        Subscription subscription = RepositoryProvider.provideApiRepository()
                .things(id)
                .compose(mLifecycleHandler.reload(R.id.thing_request))
                .subscribe(mRoomDetailsView::showThings, mRoomDetailsView::showError);

        mCompositeSubscription.add(subscription);
    }

    public void onItemChange(@NonNull Thing thing) {
        ActionParams actionParams = new ActionParams();

        Body body = new Body("toggle", thing.getId(), actionParams);

        Message message = new Message(body);

        mCompositeSubscription.clear();

        Subscription subscription = RepositoryProvider.provideApiRepository()
                .action(message)
                .doOnSubscribe(mRoomDetailsView::showLoading)
                .doOnTerminate(mRoomDetailsView::hideLoading)
                .compose(mLifecycleHandler.reload(R.id.action_request))
                .subscribe(message1 -> {}, mRoomDetailsView::showError);

        mCompositeSubscription.add(subscription);
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.clear();
    }
}
