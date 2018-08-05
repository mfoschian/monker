package it.mfx.monker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.mfx.monker.MyApp;
import it.mfx.monker.R;
import it.mfx.monker.models.Tag;
import it.mfx.monker.ui.Utils;

public class TagFormActivity extends AppCompatActivity {

    public final static String PARM_TAG_ID = "tag_id";
    public final static String PARM_PARENT_TAG_ID = "parent_id";

    TextView mLabel;
    Spinner mParent;

    Tag mTag;

    private class TagLittle {
        public String id;
        public String label;

        public TagLittle(String label, String id) {
            this.id = id;
            this.label = label;
        }

        public TagLittle(Tag t) {
            id = t.id;
            label = t.label;
        }

        @Override
        public String toString() { return (label == null ? "" : label); }
    }

    ArrayList<TagLittle> tags = new ArrayList<>();
    TagLittle noParentTag;

    class TagsAdapter extends ArrayAdapter<Tag> {

        public TagsAdapter(@NonNull Context context, @NonNull List objects) {
            super(context, android.R.layout.simple_spinner_item, objects);
        }
    }

    TagsAdapter adapter;

    private void loadTags() {
        // Don't execute in the UI thread
        MyApp app = (MyApp)getApplication();
        List<Tag> tags = app.getTags();

        loadTags(tags);
    }

    private void loadTagsAsync(final MyApp.CallbackSimple cb ) {
        MyApp app = (MyApp)getApplication();
        app.getTagsAsync(new MyApp.Callback<List<Tag>>() {
            @Override
            public void onSuccess(List<Tag> result) {
                loadTags(result);
                cb.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                cb.onError(e);
            }
        });
    }

    private void loadTags( List<Tag> tags ) {
        this.tags.clear();
        this.tags.add(noParentTag);
        for( Tag tag: tags ) {
            if( mTag.id == null || !mTag.id.equals(tag.id) )
                this.tags.add( new TagLittle(tag) );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_form);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if( bar != null ) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mLabel = findViewById(R.id.tag_label);
        mParent = findViewById(R.id.tag_parent);

        noParentTag = new TagLittle(getString(R.string.parent_tag_none_label),null);
        tags.add(noParentTag);

        adapter = new TagsAdapter(this, tags);
        mParent.setAdapter(adapter);

        final MyApp app = (MyApp)getApplication();

        String tag_id = null;
        String parent_id = null;

        Intent intent = getIntent();
        if( intent != null ) {
            tag_id = intent.getStringExtra(PARM_TAG_ID);
            parent_id = intent.getStringExtra(PARM_PARENT_TAG_ID);
        }
        if( tag_id == null && savedInstanceState != null ) {
            tag_id = savedInstanceState.getString(PARM_TAG_ID);
        }
        if( parent_id == null && savedInstanceState != null ) {
            parent_id = savedInstanceState.getString(PARM_PARENT_TAG_ID);
        }

        if( tag_id != null ) {
            app.getTagByIdAsync(tag_id, new MyApp.Callback<Tag>() {
                @Override
                public void onSuccess(Tag result) {
                    mTag = result;
                    loadTags();
                    final Tag tag = mTag;
                    Utils.runOnUIthread(new Utils.UICallback() {
                        @Override
                        public void onUIReady() {
                            render( tag );
                        }
                    });

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        else {
            mTag = new Tag();
            mTag.parent_id = parent_id;
            if( parent_id != null ) {
                app.getTagByIdAsync(parent_id, new MyApp.Callback<Tag>() {
                    @Override
                    public void onSuccess(Tag result) {
                        loadTags();
                        final Tag tag = mTag;
                        Utils.runOnUIthread(new Utils.UICallback() {
                            @Override
                            public void onUIReady() {
                                render( tag );
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
            else {
                loadTagsAsync(new MyApp.CallbackSimple() {
                    @Override
                    public void onSuccess() {
                        final Tag tag = mTag;
                        Utils.runOnUIthread(new Utils.UICallback() {
                            @Override
                            public void onUIReady() {
                                render( tag );
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menu_id = item.getItemId();
        MyApp app = (MyApp)getApplication();

        if( menu_id == R.id.menu_save ) {
            if( mTag == null )
                mTag = new Tag();

            getValuesInto(mTag);

            app.saveAsync(mTag, new MyApp.Callback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    showSaved();
                }

                @Override
                public void onError(Exception e) {
                    showError(e);
                }
            });
        }
        else if( menu_id == R.id.menu_delete ) {
            Utils.confirm(this, getString(R.string.tag_delete_confirm), android.R.drawable.ic_dialog_alert, new Utils.ConfirmListener() {
                        @Override
                        public void onPressed() {
                            deleteItem();
                        }
                    });

        }
        else if( menu_id == android.R.id.home ) {
            closeForm();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeForm() {
        setResult(RESULT_OK);
        finish();
    }

    void deleteItem() {
        MyApp app = (MyApp)getApplication();

        app.delAsync(mTag, new MyApp.CallbackSimple() {
            @Override
            public void onSuccess() {
                showDeleted();
                closeForm();
            }

            @Override
            public void onError(Exception e) {
                showError(e);
            }
        });

    }

    void showMsg(final String msg) {
        Utils.showMsg(this,msg);
    }

    void showSaved() {
        showMsg("Tag Saved");
    }

    void showDeleted() {
        showMsg("Tag Deleted");
    }

    void showError(Exception e) {
        showMsg(e.getMessage());
    }

    void getValuesInto(Tag tag) {
        tag.label = mLabel.getText().toString();
        //event.dt_start = new Date();
        //event.dt_start = Date.
        //(mDtStart.getText().toString());
        //event.dt_end = mDtStart.getText().toString();
        TagLittle t = (TagLittle)mParent.getSelectedItem();
        if( t != null ) {
            tag.parent_id = t.id;
        }
    }

    void render(Tag tag) {
        String label = tag.label;
        if( label == null )
            label = "";

        mLabel.setText(label);

        // Set selected item in the spinner
        int selection = 0; // no parent
        if( adapter != null && tag.parent_id != null ) {
            for( int i=1; i < tags.size(); i++ ) {
                TagLittle t = tags.get(i);
                if( tag.parent_id.equals(t.id)) {
                    selection = i;
                    break;
                }
            }
        }
        mParent.setSelection(selection);
    }

    //

}
