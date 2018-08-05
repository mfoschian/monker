package it.mfx.monker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



@Entity(tableName = "tags",
        indices = {
                @Index(value = "priority"),
                @Index(value = "parent_id"),
                @Index(value = "label")
        })
public class Tag {

    @PrimaryKey //(autoGenerate = true)
    @NonNull
    public String id;

    public String label;
    public String color;
    public String icon;
    public long priority = 0;

    public String parent_id = null;

}
