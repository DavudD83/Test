package space.dotcat.assistant.repository.thingsRepository;


import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Thing;

public interface ThingRepository {

    /**
     * Get things by placement's id. It checks if there are things locally, if not it will load it from
     * the server and save to db
     *
     * @param id - placement's id
     * @return flowable which contains list of things
     */

    Flowable<List<Thing>> getThingsById(@NonNull String id);

    /**
     * Reload things from the remote api and update local things
     *
     * @param id placement's id
     * @return flowable which contains list of things from the remote api
     */

    Flowable<List<Thing>> refreshThings(@NonNull String id);

    /**
     * Do action with particular thing
     *
     * @param id - thing id
     * @param message - info what should you do with thing
     * @return single with answer from the server
     */

    Single<ResponseActionMessage> doAction(@NonNull String id, @NonNull Message message);

    /**
     * Update thing in database
     *
     * @param thing - new updated thing
     * @return - completable with info was update successful or not
     */

    Completable updateThing(@NonNull Thing thing);
}
