package it.mfx.monker.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;
import java.util.UUID;

import it.mfx.monker.models.Event;
import it.mfx.monker.models.Move;
import it.mfx.monker.models.Tag;

@Database(entities = {Move.class, Tag.class, Event.class}, version = 1, exportSchema = true)
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

    /*
    public void saveItem( Item item ) {
        moveDao().updateAll(item);
    }

    public Item addItem( Item item ) {
        if( item.id == null ) {
            item.id = newId();
        }
        moveDao().insertAll(item);

        return item;
    }

    public LiveData<List<Item>> getLiveItems() { return moveDao().getAll(); }

    public List<Item> getItems() { return moveDao().getAllSync(); }

    public Item getItem(String item_id) {
        return moveDao().findById(item_id);
    }

    public boolean hasShopItem(String item_id) {
        int count  = tagDao().countForItem(item_id);
        return count > 0;
    }

    public void deleteItem(Item item) {
        if( item == null )
            return;

        moveDao().delete(item);
    }



    public ShopItem addShopItem( ShopItem item ) {
        if( item.sid == null ) {
            item.sid = newId();
        }
        tagDao().insertAll(item);
        return item;
    }

    public void deleteShopItem(ShopItem item) {
        tagDao().delete(item);
    }


    public LiveData<List<ShopItem>> getShopItemsLive() { return tagDao().getActive(); }

    public List<ShopItem> getShopItems() { return tagDao().getAllSync(); }
    public List<ShopItem> getActiveShopItems() { return tagDao().getActiveSync(); }

    public List<ShopItem> getActiveShopItemsByZone() { return tagDao().getActiveByZoneSync(); }

    public void saveShopItems(List<ShopItem> shopitems ) {
        tagDao().update(shopitems);
    }
    public void saveShopItem(ShopItem shopitem) {
        tagDao().update(shopitem);
    }

    public List<String> getActiveShopNames() {
        return eventDao().getNamesSync();
    }

    public List<String> getZoneNames(String shop_id) {
        return eventDao().getZoneNamesSync(shop_id);
    }

    */
}
