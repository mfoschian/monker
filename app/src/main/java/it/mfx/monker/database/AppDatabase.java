package it.mfx.monker.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;
import java.util.UUID;

import it.mfx.monker.models.Event;
import it.mfx.monker.models.Move;
import it.mfx.monker.models.Tag;

@Database(entities = {Move.class, Tag.class, Event.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static String dbName = "shopaholicDB";

    public abstract MoveDao moveDao();
    public abstract TagDao tagDao();
    public abstract EventDao eventDao();

    public static AppDatabase newInstance(Context context) {

        //Migration M_09_10 = new Migration_09_to_10();
        //Migration M_10_11 = new Migration_10_to_11();


        RoomDatabase.Builder<AppDatabase> b = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName);
        AppDatabase db = b

                //.addMigrations( M_09_10, M_10_11  )
                .fallbackToDestructiveMigration()
                .build();

        return db;
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    //
    // Moves
    //
    public void save( Move item ) {
        if( item.id == null )
            add(item);
        else
            moveDao().updateAll(item);
    }

    public Move add( Move item ) {
        if( item.id == null ) {
            item.id = newId();
        }
        moveDao().insertAll(item);

        return item;
    }

    public void del(Move item) {
        if( item == null )
            return;

        moveDao().delete(item);
    }

    public Move getMove(String id) {
        return moveDao().findById(id);
    }

    public List<Move> getMoves() { return moveDao().getAllSync(); }

    public List<Move> getMovesOfTag( String tag_id ) {
        return tagDao().getMovesOf(tag_id);
    }

    public List<Move> getMovesOfEvent( String event_id ) {
        return eventDao().getMovesOf(event_id);
    }


    //
    // Tags
    //
    public void save( Tag item ) {
        if( item.id == null )
            add(item);
        else
            tagDao().updateAll(item);
    }

    public Tag add( Tag item ) {
        if( item.id == null ) {
            item.id = newId();
        }
        tagDao().insertAll(item);

        return item;
    }

    public void del(Tag item) {
        if( item == null )
            return;

        tagDao().delete(item);
    }

    public Tag getTag(String id) {
        return tagDao().findById(id);
    }

    public List<Tag> getTags() { return tagDao().getAllSync(); }


    //
    // Events
    //
    public void save( Event item ) {
        if( item.id == null )
            add(item);
        else
            eventDao().updateAll(item);
    }

    public Event add( Event item ) {
        if( item.id == null ) {
            item.id = newId();
        }
        eventDao().insertAll(item);

        return item;
    }

    public void del(Event item) {
        if( item == null )
            return;

        eventDao().delete(item);
    }

    public Event getEvent(String id) {
        return eventDao().findById(id);
    }

    public List<Event> getEvents() { return eventDao().getAllSync(); }



}
