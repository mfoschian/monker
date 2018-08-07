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
import it.mfx.monker.models.Tag;

@Dao
public interface TagDao {
    @Query("SELECT * FROM tags order by priority desc, label")
    List<Tag> getAllSync();

    @Query("SELECT t.*, t2.childs"
            + " FROM tags t "
            + " LEFT OUTER JOIN "
            + "(SELECT parent_id, count(*) as childs FROM tags GROUP BY parent_id)"
            + " t2 "
            + " ON t2.parent_id = t.id"
            + " WHERE t.parent_id = :parent_id "
            + " ORDER BY t.priority desc, t.label")
    List<Tag> getAllSync(String parent_id);

    @Query("SELECT t.*, t2.childs"
            + " FROM tags t "
            + " LEFT OUTER JOIN "
            + "(SELECT parent_id, count(*) as childs FROM tags GROUP BY parent_id)"
            + " t2 "
            + " ON t2.parent_id = t.id"
            + " WHERE t.parent_id IS NULL "
            + " ORDER BY t.priority desc, t.label")
    List<Tag> getAllRootsSync();



    @Query("SELECT * FROM tags order by priority desc, label")
    LiveData<List<Tag>> getAll();

    @Query("SELECT * FROM tags where id LIKE :id")
    Tag findById(String id);

    @Query("SELECT * FROM tags where parent_id LIKE :tag_id")
    List<Tag> findByParent(String tag_id);

    @Query("SELECT COUNT(*) from tags")
    int countItems();

    @Query("SELECT COUNT(*) from moves m left join tags t on (m.tag_id = t.id) where m.tag_id = :tag_id")
    int countMovesOf(String tag_id);

    @Query("SELECT m.* from moves m left join tags t on (m.tag_id = t.id) where m.tag_id = :tag_id")
    List<Move> getMovesOf(String tag_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Tag... items);

    @Update
    void updateAll(Tag... items);

    @Delete
    void delete(Tag item);
}
