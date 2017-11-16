package space.dotcat.assistant.repository;

import android.support.annotation.NonNull;

import io.realm.Realm;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Url;


public class DefaultAuthRepository implements AuthRepository {

    @Override
    public void saveAuthorizationAnswer(@NonNull AuthorizationAnswer answer) {
        Realm realmInstance = Realm.getDefaultInstance();

        realmInstance.executeTransaction(transaction -> {
            transaction.delete(AuthorizationAnswer.class);
            transaction.insert(answer);
        });
    }

    @Override
    public String token() {
        Realm realmInstance = Realm.getDefaultInstance();

        AuthorizationAnswer authorizationAnswer = realmInstance.where(AuthorizationAnswer.class)
                .findFirst();

        if(authorizationAnswer == null)
            return "";

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
    public void saveUrl(@NonNull Url url) {
        Realm realmInstance = Realm.getDefaultInstance();

        realmInstance.executeTransaction(realm -> {
            realm.delete(Url.class);
            realm.insert(url);
        });
    }

    @NonNull
    @Override
    public String url() {
        Realm realmInstance = Realm.getDefaultInstance();

        Url url = realmInstance.where(Url.class).findFirst();
        //TODO look at it line later
        return url.getUrl();
    }
}
