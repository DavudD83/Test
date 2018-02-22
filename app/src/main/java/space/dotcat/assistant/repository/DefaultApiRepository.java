package space.dotcat.assistant.repository;


import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.RoomResponse;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.ThingResponse;
import space.dotcat.assistant.utils.RxUtils;


public class DefaultApiRepository implements ApiRepository {

    @NonNull
    @Override
    public Single<AuthorizationAnswer> auth(@NonNull Authorization authorizationInfo) {
        return ApiFactory.getApiService()
                .auth(authorizationInfo)
                .flatMap(authorizationAnswer -> {
                    RepositoryProvider.provideAuthRepository()
                            .saveAuthorizationAnswer(authorizationAnswer);

                    ApiFactory.recreate();

                    return Single.just(authorizationAnswer);
                })
                .doOnError(throwable -> ApiFactory.deleteInstance())
                .compose(RxUtils.makeSingleAsyncWithUiCallback());
    }

    @NonNull
    @Override
    public Observable<List<Room>> rooms() {
        return ApiFactory.getApiService()
                .rooms()
                .toObservable()
                .map(RoomResponse::getRooms)
                .flatMap(rooms -> {
                    Realm instance = Realm.getDefaultInstance();

                    instance.executeTransaction(realm -> {
                        realm.delete(Room.class);
                        realm.insert(rooms);
                    });

                    instance.close();
                    return Observable.just(rooms);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Room> rooms = realm.where(Room.class).findAll();

                    List<Room> resultRooms = realm.copyFromRealm(rooms);

                    realm.close();

                    return Observable.mergeDelayError(Observable.just(resultRooms),
                            Observable.error(throwable));
                })
                .compose(RxUtils.makeObservableAsyncWithUiCallback());
    }

    @NonNull
    @Override
    public Observable<List<Thing>> things(String id) {
        return ApiFactory.getApiService()
                .things(id)
                .toObservable()
                .map(ThingResponse::getThings)
                .flatMap(things -> {
                    Realm instance = Realm.getDefaultInstance();

                    instance.executeTransaction(realm -> realm.insertOrUpdate(things));

                    instance.close();
                    return Observable.just(things);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Thing> things = realm.where(Thing.class).equalTo("mPlacement", id).findAll();

                    List<Thing> resultThings = realm.copyFromRealm(things);

                    realm.close();

                    return Observable.mergeDelayError(Observable.just(resultThings),
                            Observable.error(throwable));
                })
                .compose(RxUtils.makeObservableAsyncWithUiCallback());
    }

    @NonNull
    @Override
    public Single<Message> action(@NonNull Message message) {
        return ApiFactory.getApiService()
                .action(message)
                .compose(RxUtils.makeSingleAsyncWithUiCallback());
    }
}
