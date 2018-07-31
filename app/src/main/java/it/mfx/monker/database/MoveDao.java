package it.mfx.monker.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.mfx.monker.models.Move;

@Dao
public interface MoveDao {
    @Query("SELECT * FROM moves order by dt desc")
    List<Move> getAllSync();

    @Query("SELECT * FROM moves order by dt desc")
    LiveData<List<Move>> getAll();

    @Query("SELECT * FROM moves where id LIKE :id")
    Move findById(String id);

    @Query("SELECT * FROM moves where tag_id LIKE :tag_id")
    List<Move> findByTag(String tag_id);

    @Query("SELECT * FROM moves where event_id LIKE :event_id")
    List<Move> findByEvent(String event_id);

    @Query("SELECT COUNT(*) from moves")
    int countItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Move... items);

    @Update
    void updateAll(Move... items);

    @Delete
    void delete(Move item);
}
