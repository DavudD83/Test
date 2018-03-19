package space.dotcat.assistant.repository;


import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;

public interface ApiRepository {

    @NonNull
    Single<AuthorizationAnswer> auth(@NonNull  Authorization authorizationInfo);

    @NonNull
    Observable<List<Room>> rooms();

    @NonNull
    Observable<List<Thing>> things(String id);

    @NonNull
    Single<ResponseActionMessage> action(@NonNull String id, @NonNull Message message);
}
