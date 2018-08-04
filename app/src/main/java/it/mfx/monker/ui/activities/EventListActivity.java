package it.mfx.monker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.mfx.monker.MyApp;
import it.mfx.monker.R;
import it.mfx.monker.models.Event;
import it.mfx.monker.ui.Utils;

public class EventListActivity extends AppCompatActivity {

    interface Listener {
        void onEventSelected(String event_id);
    }


    public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

        private final List<Event> mEvents;
        private Listener mListener;

        public EventRecyclerViewAdapter(@NonNull List<Event> items, @NonNull Listener listener) {
            mEvents = items;
            mListener = listener;
        }


        @Override
        public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_item, parent, false);
            return new EventRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final EventRecyclerViewAdapter.ViewHolder holder, int position) {
            Event ev = mEvents.get(position);
            holder.mItem = ev;
            holder.mEventLabelView.setText(ev.label);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ev_id = holder.mItem.id;
                    mListener.onEventSelected( ev_id );
                }
            });
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mEventLabelView;
            public Event mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mEventLabelView = view.findViewById(R.id.event_label);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mEventLabelView.getText() + "'";
            }
        }
    }


    MyApp app() {
        MyApp app = (MyApp)getApplication();
        return app;
    }

    private EventRecyclerViewAdapter adapter;
    private List<Event> mEvents;


    private void returnChoosedEvent(String event_id ) {
        //app().setCurrentEvent( event_id );
        Intent data = new Intent();
        data.setData(Uri.parse(event_id));
        setResult(RESULT_OK, data);
        finish();
    }

    private void addEvent() {
        Context ctx = this;
        Intent intent = new Intent(ctx, EventFormActivity.class);
        //intent.putExtra(EventFormActivity.SUGGESTED_NAME_ARG, suggestedName);
        startActivityForResult(intent, MyApp.IntentRequests.NEW_EVENT_REQUEST);
    }

    private void reloadData() {
        app().getEventsAsync(new MyApp.Callback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> result) {
                final List<Event> events = result;

                Utils.runOnUIthread(new Utils.UICallback() {
                    @Override
                    public void onUIReady() {
                        mEvents.clear();
                        mEvents.addAll(events);
                        adapter.notifyDataSetChanged();

                        if (mEvents.size() == 0) {
                            // Nessun evento scaricato ...
                            String msg = getResources().getString(R.string.no_events)
                                    //+ "." + getResources().getString(R.string.please_reload_data)
                                    ;
                            Utils.showModalMsg(EventListActivity.this, msg, new Utils.ConfirmListener() {
                                @Override
                                public void onPressed() {
                                    finish();
                                }
                            });
                        }

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if( bar != null ) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(R.string.event_list_title);
        }

        View view = findViewById(R.id.event_list);


        if (!(view instanceof RecyclerView)) {
            throw new Error("Wrong xml layout");
        }

        mEvents = new ArrayList<>();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new EventRecyclerViewAdapter(mEvents, new EventListActivity.Listener() {
            @Override
            public void onEventSelected(String event_id) {
                returnChoosedEvent(event_id);
            }
        });
        recyclerView.setAdapter(adapter);

        reloadData();

        // Get Events from db
        FloatingActionButton button = findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menu_id = item.getItemId();
        if( menu_id == android.R.id.home ) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == MyApp.IntentRequests.NEW_EVENT_REQUEST ) {
            if( resultCode == RESULT_OK ) {
                reloadData();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
