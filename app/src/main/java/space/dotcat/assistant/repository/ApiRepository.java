package space.dotcat.assistant.repository;


import android.support.annotation.NonNull;

import retrofit2.http.Header;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;

import java.util.List;

import rx.Observable;
import space.dotcat.assistant.content.Url;

public interface ApiRepository {

    @NonNull
    Observable<AuthorizationAnswer> auth(@NonNull  Authorization authorizationInfo);

    @NonNull
    Observable<List<Room>> rooms();

    @NonNull
    Observable<List<Thing>> things(String id);

    @NonNull
    Observable<Message> action(@NonNull Message message);
}
