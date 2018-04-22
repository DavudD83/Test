package space.dotcat.assistant.repository.thingsRepository.localThingsDataSource;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.assistant.content.Thing;

@Dao
public interface ThingsDao {

    /**
     * Get things by particular placement id from local database.
     * This is observable query, so each time when database will be updated,
     * flowable's onNext will be called
     *
     * @param id placement id
     * @return flowable which contains list of things from database.
     */

    @Query("Select * from Things where thing_placement = :id")
    Flowable<List<Thing>> getThingsById(String id);


    /**
     * Insert things into database. If some conflict occurs, thing will be completely replaced
     *
     * @param things list of things that you want to insert into database.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertThings(List<Thing> things);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertThing(Thing thing);

    /**
     * Delete all things from database
     */
    @Query("Delete from Things")
    void deleteAllThings();

    /**
     * Delete particular thing by its id
     *
     * @param id thing id ,that you want to delete
     */

    @Query("Delete from Things where thing_id = :id")
    void deleteThingById(String id);


    /**
     * Update your thing
     *
     * @param thing thing that you want to update
     */

    @Update
    void updateThing(Thing thing);
}
