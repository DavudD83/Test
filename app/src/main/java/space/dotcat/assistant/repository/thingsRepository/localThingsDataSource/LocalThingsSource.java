package space.dotcat.assistant.repository.thingsRepository.localThingsDataSource;


import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.assistant.content.Thing;

public interface LocalThingsSource {

    Flowable<List<Thing>> getThingsById(@NonNull String id);

    void addThingsSync(@NonNull List<Thing> things);

    void deleteAllThings();

    void updateThing(@NonNull Thing thing);

    void deleteThingById(String id);

    void insertThing(Thing thing);
}
