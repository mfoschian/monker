package it.mfx.monker.models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

import it.mfx.monker.database.DateTimeConverter;

@Entity(tableName = "events",
        indices = {
                @Index(value = "label"),
                @Index(value = "dt_start"),
                @Index(value = "dt_end")
        })
public class Event {

    @PrimaryKey //(autoGenerate = true)
    @NonNull
    public String id;

    public String label;

    @TypeConverters({DateTimeConverter.class})
    public Date dt_start;

    @TypeConverters({DateTimeConverter.class})
    public Date dt_end;

}
