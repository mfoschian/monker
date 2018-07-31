package it.mfx.monker.models;




import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Currency;
import java.util.Date;


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

    public Date dt;
    public Currency amount;
    public String description;
    public boolean outbound = true;

    public String tag_id;
    public String event_id;

}

