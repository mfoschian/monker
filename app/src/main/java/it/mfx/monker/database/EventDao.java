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
import it.mfx.monker.models.Move;

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

    @Query("SELECT COUNT(*) from moves m left join events e on (m.event_id = e.id) where m.event_id = :event_id")
    int countMovesOf(String event_id);

    @Query("SELECT m.* from moves m left join events e on (m.event_id = e.id) where m.event_id = :event_id")
    List<Move> getMovesOf(String event_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Event... items);

    @Update
    void updateAll(Event... items);

    @Delete
    void delete(Event item);
}
