package space.dotcat.assistant.repository.thingsRepository.localThingsDataSource;


import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import space.dotcat.assistant.content.Thing;

public class LocalThingsSourceImpl implements LocalThingsSource {

    private ThingsDao mThingsDao;

    @Inject
    public LocalThingsSourceImpl(ThingsDao thingsDao) {
        mThingsDao = thingsDao;
    }

    @Override
    public Flowable<List<Thing>> getThingsById(@NonNull String id) {
        return mThingsDao.getAllThingsById(id);
    }

    @Override
    public void addThingsSync(@NonNull List<Thing> things) {
        mThingsDao.insertThings(things);
    }

    @Override
    public void deleteAllThings() {
        mThingsDao.deleteAllThings();
    }

    @Override
    public void updateThing(@NonNull Thing thing) {
        mThingsDao.updateThing(thing);
    }

    @Override
    public void deleteThingById(String id) {
        mThingsDao.deleteThingById(id);
    }

    @Override
    public void insertThing(Thing thing) {
        mThingsDao.insertThing(thing);
    }

    @Override
    public void deleteAndUpdateThings(List<Thing> things) {
        mThingsDao.deleteAndInsertThings(things);
    }
}
