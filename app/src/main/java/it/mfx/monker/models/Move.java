package it.mfx.monker.models;




import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Currency;
import java.util.Date;

import it.mfx.monker.database.DateTimeConverter;


@Entity(tableName = "moves",
        indices = {
                @Index(value = "dt"),
                @Index(value = "outbound"),
                @Index(value = "tag_id"),
                @Index(value = "event_id")
        })
public class Move {

    @PrimaryKey //(autoGenerate = true)
    @NonNull
    public String id;

    @TypeConverters({DateTimeConverter.class})
    public Date dt;
    public float amount;
    public String currency = "EUR";
    public String description;
    public boolean outbound = true;

    public String tag_id;
    public String event_id;

}

