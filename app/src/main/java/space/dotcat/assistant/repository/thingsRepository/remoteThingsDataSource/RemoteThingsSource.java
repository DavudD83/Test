package space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource;


import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Thing;

public interface RemoteThingsSource {

    Flowable<List<Thing>> loadThingsByPlacementId(@NonNull String id);

    Single<ResponseActionMessage> doActionOnThing(@NonNull String id, @NonNull Message message);
}
