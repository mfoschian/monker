package it.mfx.monker.ui.activities;

import android.content.Context;
import android.content.Intent;
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
import it.mfx.monker.models.Tag;
import it.mfx.monker.ui.HolderFactory;
import it.mfx.monker.ui.ListRecyclerViewAdapter;
import it.mfx.monker.ui.Utils;

public class TagListActivity extends AppCompatActivity {


    interface Listener {
        void onItemSelected(String tag_id);
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTagLabelView;
        public Tag mItem;

        public TagViewHolder(View view) {
            super(view);
            mView = view;
            mTagLabelView = view.findViewById(R.id.tag_label);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTagLabelView.getText() + "'";
        }
    }


    MyApp app() {
        MyApp app = (MyApp)getApplication();
        return app;
    }

    private ListRecyclerViewAdapter<Tag, TagViewHolder, Listener> adapter;
    private List<Tag> mTags;


    private void onChoosedTag(String tag_id ) {
        //Intent data = new Intent();
        //data.setData(Uri.parse(tag_id));
        //setResult(RESULT_OK, data);
        //finish();
        Context ctx = this;
        Intent intent = new Intent(ctx, TagFormActivity.class);
        intent.putExtra(TagFormActivity.PARM_TAG_ID, tag_id);
        startActivityForResult(intent, MyApp.IntentRequests.EDIT_TAG_REQUEST);
    }

    private void addTag() {
        Context ctx = this;
        Intent intent = new Intent(ctx, TagFormActivity.class);
        //intent.putExtra(TagFormActivity.SUGGESTED_NAME_ARG, suggestedName);
        startActivityForResult(intent, MyApp.IntentRequests.EDIT_TAG_REQUEST);

    }

    private void reloadData() {
        app().getTagsAsync(new MyApp.Callback<List<Tag>>() {
            @Override
            public void onSuccess(List<Tag> result) {
                final List<Tag> tags = result;

                Utils.runOnUIthread(new Utils.UICallback() {
                    @Override
                    public void onUIReady() {
                        mTags.clear();
                        mTags.addAll(tags);
                        adapter.notifyDataSetChanged();

                        if( mTags.size() == 0 ) {
                            // Nessun tag scaricato ...
                            String msg =getResources().getString(R.string.no_tags)
                                    //+ "." + getResources().getString(R.string.please_reload_data)
                                    ;
                            Utils.showModalMsg(TagListActivity.this, msg, new Utils.ConfirmListener() {
                                @Override
                                public void onPressed() {
                                    //finish();
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
        setContentView(R.layout.tag_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if( bar != null ) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(R.string.tag_list_title);
        }

        View view = findViewById(R.id.tag_list);

        if (! (view instanceof RecyclerView) ) {
            throw new Error("Wrong xml layout");
        }

        mTags = new ArrayList<>();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new ListRecyclerViewAdapter<Tag, TagViewHolder, Listener>(mTags, R.layout.tag_list_item,
                new HolderFactory<TagViewHolder>() {
                    @Override
                    public TagViewHolder createHolder(View view) {
                        return new TagViewHolder(view);
                    }
                },
                new ListRecyclerViewAdapter.Binder<Tag, TagViewHolder, Listener>() {
                    @Override
                    public void bind(Tag item, final TagViewHolder holder, final Listener listener) {
                        holder.mItem = item;
                        holder.mTagLabelView.setText(item.label);

                        if( listener != null ) {
                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String ev_id = holder.mItem.id;
                                    listener.onItemSelected(ev_id);
                                }
                            });
                        }
                    }
                },
                new Listener() {
                        @Override
                        public void onItemSelected (String tag_id){
                        onChoosedTag(tag_id);
                    }
        });

        recyclerView.setAdapter(adapter);


        // Get Events from db
        reloadData();


        FloatingActionButton button = findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag();
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

        if( requestCode == MyApp.IntentRequests.EDIT_TAG_REQUEST) {
            if( resultCode == RESULT_OK ) {
                reloadData();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
