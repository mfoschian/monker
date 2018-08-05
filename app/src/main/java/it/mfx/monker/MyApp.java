package it.mfx.monker;

import android.app.Application;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import it.mfx.monker.database.AppDatabase;
import it.mfx.monker.models.Event;
import it.mfx.monker.models.Move;
import it.mfx.monker.models.Tag;

public class MyApp extends Application {
    private AppDatabase db = null;


    public final class IntentRequests {
        final public static int CHOOSE_ITEM_REQUEST = 8000;
        final public static int EDIT_ITEM_REQUEST = 8001;
        final public static int SHOP_RUN_REQUEST = 8002;
        final public static int PERMISSIONS_REQUEST = 8003;
        final public static int CHOOSE_IMPORT_FILE_REQUEST = 8004;
        final public static int EDIT_EVENT_REQUEST = 8005;
        final public static int EDIT_TAG_REQUEST = 8006;
    }

    AppDatabase db() {
        if (db == null) {
            db = AppDatabase.newInstance(this.getApplicationContext());
        }
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public interface Callback<T> {
        void onSuccess(T result);

        void onError(Exception e);
    }

    public interface CallbackSimple {
        void onSuccess();

        void onError(Exception e);
    }


    //==============================================
    //  Events
    //==============================================
    public List<Event> getEvents() {
        List<Event> res = db().eventDao().getAllSync();
        return res;
    }

    public void getEventsAsync(@NonNull final Callback<List<Event>> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Event> res = getEvents();
                    cb.onSuccess(res);
                } catch (Exception err) {
                    cb.onError(err);
                }
            }
        });
    }

    public void getEventByIdAsync(@NonNull final String event_id, @NonNull final Callback<Event> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Event res = db().eventDao().findById(event_id);
                    cb.onSuccess(res);
                } catch (Exception err) {
                    cb.onError(err);
                }
            }
        });
    }

    public void save(Event event) {
        db().save(event);
    }

    public void saveAsync(final Event event, final Callback<Boolean> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    save(event);
                    if (cb != null)
                        cb.onSuccess(true);
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });
    }

    public void add(Event event) {
        db().add(event);
    }

    public void addAsync(final Event event, final Callback<Boolean> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    add(event);
                    if (cb != null)
                        cb.onSuccess(true);
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });
    }

    public void del(Event event) {
        db().del(event);
    }

    public void delAsync( final Event object, final CallbackSimple cb ) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    del(object);
                    if (cb != null)
                        cb.onSuccess();
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });

    }



    //==============================================
    //  Tags
    //==============================================
    public List<Tag> getTags() {
        List<Tag> res = db().tagDao().getAllSync();
        return res;
    }

    public void getTagsAsync(@NonNull final Callback<List<Tag>> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Tag> res = getTags();
                    cb.onSuccess(res);
                } catch (Exception err) {
                    cb.onError(err);
                }
            }
        });
    }

    public void getTagByIdAsync(@NonNull final String tag_id, @NonNull final Callback<Tag> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Tag res = db().tagDao().findById(tag_id);
                    cb.onSuccess(res);
                } catch (Exception err) {
                    cb.onError(err);
                }
            }
        });
    }

    public void save(Tag tag) {
        db().save(tag);
    }

    public void saveAsync(final Tag tag, final Callback<Boolean> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    save(tag);
                    if (cb != null)
                        cb.onSuccess(true);
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });
    }

    public void add(Tag tag) {
        db().add(tag);
    }

    public void addAsync(final Tag tag, final Callback<Boolean> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    add(tag);
                    if (cb != null)
                        cb.onSuccess(true);
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });
    }

    public void del(Tag tag) {
        db().del(tag);
    }

    public void delAsync( final Tag object, final CallbackSimple cb ) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    del(object);
                    if (cb != null)
                        cb.onSuccess();
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });

    }





    //==============================================
    //  Moves
    //==============================================
    public List<Move> getMoves() {
        List<Move> res = db().moveDao().getAllSync();
        return res;
    }

    public void getMovesAsync(@NonNull final Callback<List<Move>> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Move> res = getMoves();
                    cb.onSuccess(res);
                } catch (Exception err) {
                    cb.onError(err);
                }
            }
        });
    }

    public void getMoveByIdAsync(@NonNull final String id, @NonNull final Callback<Move> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Move res = db().moveDao().findById(id);
                    cb.onSuccess(res);
                } catch (Exception err) {
                    cb.onError(err);
                }
            }
        });
    }

    public void save(Move move) {
        db().save(move);
    }

    public void saveAsync(final Move move, final Callback<Boolean> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    save(move);
                    if (cb != null)
                        cb.onSuccess(true);
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });
    }

    public void add(Move move) {
        db().add(move);
    }

    public void addAsync(final Move move, final Callback<Boolean> cb) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    add(move);
                    if (cb != null)
                        cb.onSuccess(true);
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });
    }

    public void del(Move move) {
        db().del(move);
    }


    public void delAsync( final Move object, final CallbackSimple cb ) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    del(object);
                    if (cb != null)
                        cb.onSuccess();
                } catch (Exception err) {
                    if (cb != null)
                        cb.onError(err);
                }
            }
        });

    }

}
