package space.dotcat.assistant.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import space.dotcat.assistant.content.AuthorizationAnswer;

public class AuthorizationUtils {

    private AuthorizationUtils(){
    }

    public static void saveAuthAnswer(@NonNull AuthorizationAnswer authorizationAnswer){

        Realm realmInstance = Realm.getDefaultInstance();

        realmInstance.executeTransaction(transaction -> {
            transaction.delete(AuthorizationAnswer.class);
            transaction.insert(authorizationAnswer);
        });

        realmInstance.close();
    }

    public static String getToken() {

        Realm realmInstance = Realm.getDefaultInstance();

        AuthorizationAnswer authorizationAnswer = realmInstance.where(AuthorizationAnswer.class)
                .findFirst();

        if(authorizationAnswer == null) {
            return null;
        }

        return authorizationAnswer.getToken();
    }
}
