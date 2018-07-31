package it.mfx.monker.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.mfx.monker.models.Tag;

@Dao
public interface TagDao {
    @Query("SELECT * FROM tags order by priority desc")
    List<Tag> getAllSync();

    @Query("SELECT * FROM tags order by priority desc")
    LiveData<List<Tag>> getAll();

    @Query("SELECT * FROM tags where id LIKE :id")
    Tag findById(String id);

    @Query("SELECT * FROM tags where parent_id LIKE :tag_id")
    List<Tag> findByParent(String tag_id);

    @Query("SELECT COUNT(*) from tags")
    int countItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Tag... items);

    @Update
    void updateAll(Tag... items);

    @Delete
    void delete(Tag item);
}
