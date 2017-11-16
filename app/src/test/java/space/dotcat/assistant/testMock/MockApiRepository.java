package space.dotcat.assistant.testMock;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import space.dotcat.assistant.content.Authorization;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.ApiRepository;

public class MockApiRepository implements ApiRepository{
    @NonNull
    @Override
    public Observable<AuthorizationAnswer> auth(@NonNull Authorization authorizationInfo) {
        return Observable.empty();
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
    public Observable<Message> action(@NonNull Message message) {
        return Observable.empty();
    }
}
