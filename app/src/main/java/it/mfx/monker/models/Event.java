package it.mfx.monker.models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "events",
        indices = {
                @Index(value = "priority"),
                @Index(value = "dt_start"),
                @Index(value = "dt_end")
        })
public class Event {

    @PrimaryKey //(autoGenerate = true)
    @NonNull
    public String id;

    public String label;
    public Date dt_start;
    public Date dt_end;

}
