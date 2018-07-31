package it.mfx.monker.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateTimeConverter {

    @TypeConverter
    public static long toLong( Date dt ) {
        if( dt == null )
            dt = new Date();

        return dt.getTime();
    }

    @TypeConverter
    public static Date toDate( long l ) {
        return new Date(l);
    }

}
