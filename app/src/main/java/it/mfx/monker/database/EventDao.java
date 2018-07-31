package it.mfx.monker.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.mfx.monker.models.Event;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events order by dt_start desc")
    List<Event> getAllSync();

    @Query("SELECT * FROM events order by dt_start desc")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM events where id LIKE :id")
    Event findById(String id);

    @Query("SELECT COUNT(*) from events")
    int countItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Event... items);

    @Update
    void updateAll(Event... items);

    @Delete
    void delete(Event item);
}
