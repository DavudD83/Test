package space.dotcat.assistant.repository.thingsRepository;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.repository.thingsRepository.localThingsDataSource.LocalThingsSource;
import space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource.RemoteThingsSource;
import space.dotcat.assistant.utils.RxUtils;

public class ThingRepositoryImpl implements ThingRepository {

    private LocalThingsSource mLocalThingsSource;

    private RemoteThingsSource mRemoteThingsSource;

    @Inject
    public ThingRepositoryImpl(LocalThingsSource localThingsSource, RemoteThingsSource remoteThingsSource) {
        mLocalThingsSource = localThingsSource;

        mRemoteThingsSource = remoteThingsSource;
    }

    @Override
    public Flowable<List<Thing>> getThingsById(@NonNull String id) {
        return mLocalThingsSource.getThingsById(id);
    }

    @Override
    public Flowable<List<Thing>> refreshThings(@NonNull String id) {
        return mRemoteThingsSource.loadThingsByPlacementId(id)
                .doOnNext(things -> {
                    if (things.isEmpty()) {
                        return;
                    }

                    mLocalThingsSource.deleteAndUpdateThings(things);
                    //TODO delete All things in placement by id, and insert new things to database
                });
    }

    @Override
    public Single<ResponseActionMessage> doAction(@NonNull String id, @NonNull Message message) {
        return mRemoteThingsSource.doActionOnThing(id, message);
    }

    @Override
    public Completable updateThing(@NonNull Thing thing) {
        return Completable.fromAction(()-> mLocalThingsSource.updateThing(thing));
    }

    private Flowable<List<Thing>> loadThingsAndSaveToDb(@NonNull String id) {
        return mRemoteThingsSource.loadThingsByPlacementId(id)
                .doOnNext(things -> {
                    if(things.isEmpty()) {
                        return;
                    }

                    mLocalThingsSource.addThingsSync(things);
                });
    }
}
