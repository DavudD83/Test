package space.dotcat.assistant.repository.thingsRepository.remoteThingsDataSource;


import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.assistant.api.ApiFactory;
import space.dotcat.assistant.content.Message;
import space.dotcat.assistant.content.ResponseActionMessage;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.content.ThingResponse;

public class RemoteThingsSourceImpl implements RemoteThingsSource {

    private ApiFactory mApiFactory;

    @Inject
    public RemoteThingsSourceImpl(ApiFactory apiFactory) {
        mApiFactory = apiFactory;
    }

    @Override
    public Flowable<List<Thing>> loadThingsByPlacementId(@NonNull String id) {
        return mApiFactory.getApiService()
                .things(id)
                .toFlowable()
                .map(ThingResponse::getThings);
    }

    @Override
    public Single<ResponseActionMessage> doActionOnThing(@NonNull String id, @NonNull Message message) {
        return mApiFactory.getApiService()
                .action(id, message);
    }
}
