package space.dotcat.assistant.testMock;

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
import space.dotcat.assistant.repository.ApiRepository;

public class MockApiRepository implements ApiRepository{
    @NonNull
    @Override
    public Single<AuthorizationAnswer> auth(@NonNull Authorization authorizationInfo) {
        return Single.never();
    }

    @NonNull
    @Override
    public Observable<List<Room>> rooms() {
        return Observable.empty();
    }

    @NonNull
    @Override
    public Observable<List<Thing>> things(String id) {
        return Observable.empty();
    }

    @NonNull
    @Override
    public Single<ResponseActionMessage> action(@NonNull String id, @NonNull Message message) {
        return Single.never();
    }
}
