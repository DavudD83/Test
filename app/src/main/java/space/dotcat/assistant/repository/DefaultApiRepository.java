package space.dotcat.assistant.repository;


import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmResults;

import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.RoomResponse;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.ThingResponse;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.utils.RxUtils;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.content.Room;

import java.util.List;

import rx.Observable;


public class DefaultApiRepository implements ApiRepository {

    @NonNull
    @Override
    public Observable<AuthorizationAnswer> auth(@NonNull Authorization authorizationInfo) {
        return ApiFactory.getApiService()
                .auth(authorizationInfo)
                .flatMap(authorizationAnswer -> {
                    Realm realmInstance = Realm.getDefaultInstance();

                    realmInstance.executeTransaction(transaction -> {
                        transaction.delete(AuthorizationAnswer.class);
                        transaction.insert(authorizationAnswer);
                    });

                    ApiFactory.recreate();

                    realmInstance.close();

                    return Observable.just(authorizationAnswer);
                })
                .doOnError(throwable -> {
                    ApiFactory.deleteInstance();
                })
                .compose(RxUtils.async());
    }

    @NonNull
    @Override
    public Observable<List<Room>> rooms() {
        return ApiFactory.getApiService()
                .rooms()
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
                .compose(RxUtils.async());
    }

    @NonNull
    @Override
    public Observable<List<Thing>> things(String id) {
        return ApiFactory.getApiService()
                .things(id)
                .map(ThingResponse::getThings)
                .flatMap(things -> {
                    Realm instance = Realm.getDefaultInstance();

                    instance.executeTransaction(realm ->{
                        realm.insertOrUpdate(things);
                    });

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
                .compose(RxUtils.async());
    }

    @NonNull
    @Override
    public Observable<Message> action(@NonNull Message message) {
        return ApiFactory.getApiService()
                .action(message)
                .compose(RxUtils.async());
    }


    @Override
    public String token() {
        Realm realmInstance = Realm.getDefaultInstance();

        AuthorizationAnswer authorizationAnswer = realmInstance.where(AuthorizationAnswer.class)
                .findFirst();

        if(authorizationAnswer == null)
            return null;

        return authorizationAnswer.getToken();
    }

    @Override
    public void deleteToken() {
        Realm realmInstance = Realm.getDefaultInstance();

        realmInstance.executeTransaction(transaction -> {
            transaction.delete(AuthorizationAnswer.class);
        });
    }

    @Override
    public void saveUrl(Url url) {
        Realm realmInstance = Realm.getDefaultInstance();

        realmInstance.executeTransaction(realm -> {
            realm.delete(Url.class);
            realm.insert(url);
        });

        realmInstance.close();
    }

    @NonNull
    @Override
    public String url() {
        Realm realmInstance = Realm.getDefaultInstance();

        Url url = realmInstance.where(Url.class).findFirst();

        return url.getUrl();
    }
}
