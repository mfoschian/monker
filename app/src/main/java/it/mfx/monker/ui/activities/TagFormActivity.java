package it.mfx.monker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import it.mfx.monker.MyApp;
import it.mfx.monker.R;
import it.mfx.monker.models.Tag;
import it.mfx.monker.ui.Utils;

public class TagFormActivity extends AppCompatActivity {

    public final static String PARM_TAG_ID = "tag_id";

    TextView mLabel;

    Tag mTag;


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

        MyApp app = (MyApp)getApplication();

        String tag_id = null;

        Intent intent = getIntent();
        if( intent != null ) {
            tag_id = intent.getStringExtra(PARM_TAG_ID);
        }
        else if( tag_id != null ) {
            tag_id = savedInstanceState.getParcelable(PARM_TAG_ID);
        }

        if( tag_id != null ) {
            app.getTagByIdAsync(tag_id, new MyApp.Callback<Tag>() {
                @Override
                public void onSuccess(Tag result) {
                    mTag = result;
                    render( result );
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        else {
            mTag = new Tag();
            render(mTag);
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
    }

    void render(Tag tag) {
        String label = tag.label;
        if( label == null )
            label = "";

        mLabel.setText(label);
    }

    //

}
