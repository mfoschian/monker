package it.mfx.monker.ui.activities;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.List;

import it.mfx.monker.MyApp;
import it.mfx.monker.R;
import it.mfx.monker.models.Event;
import it.mfx.monker.models.Move;
import it.mfx.monker.models.Tag;
import it.mfx.monker.ui.Formatters;
import it.mfx.monker.ui.Utils;
import it.mfx.monker.ui.fragments.TagChooserFragment;

public class MoveActivity extends AppCompatActivity implements TagChooserFragment.Listener {

    public final static String PARM_MOVE_ID = "move_id";


    /*
    void backToPreviousFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        int stack_size = fragmentManager.getBackStackEntryCount();
        if( stack_size > 1 )
            fragmentManager.popBackStack();
    }
    */
    Move mMove;

    TextView mTagLabel;
    TextView mAmount;


    void changeFragmentTo(String parent_id) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        TagChooserFragment newF = null;

        List<Fragment> fragments = fragmentManager.getFragments();
        for( Fragment fragment: fragments ) {
            TagChooserFragment f = (TagChooserFragment)fragment;
            if( f != null ) {
                String pid = f.getParentTagId();
                if( (pid == null && parent_id == null)
                    || (pid != null && pid.equals(parent_id) ) ) {
                    newF = f;
                    break;
                }
            }
        }

        if( newF == null ) {
            newF = TagChooserFragment.newInstance(parent_id);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.tags_frame, newF );
        if( parent_id != null )
            transaction.addToBackStack(null);
        transaction.commit();
    }

    void setTagTo( final Tag tag ) {
        Log.i("TEST","Tag setted to " + tag.label);
        Utils.runOnUIthread(new Utils.UICallback() {
            @Override
            public void onUIReady() {
                String label;
                if( tag == null ) {
                    label = "";
                }
                else {
                    label = tag.label;
                    if( label == null )
                        label = "";
                }

                mTagLabel.setText(label);
            }
        });
    }

    @Override
    public void onTagSelected(Tag tag) {
        if( tag == null )
            return;

        if( tag.childs_count > 0 ) {
            // switch to a new fragment
            String new_parent_id = tag.id;
            changeFragmentTo(new_parent_id);
        }
        else
            setTagTo(tag);
    }

    @Override
    public void onTagForcedSelect(Tag tag) {
        if( tag == null )
            return;

        setTagTo(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if( bar != null ) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mTagLabel = findViewById(R.id.move_tag);
        mAmount = findViewById(R.id.move_amount);

        MyApp app = (MyApp)getApplication();

        String move_id = null;

        Intent intent = getIntent();
        if( intent != null ) {
            move_id = intent.getStringExtra(PARM_MOVE_ID);
        }
        if( move_id == null && savedInstanceState != null ) {
            move_id = savedInstanceState.getString(PARM_MOVE_ID);
        }

        if( move_id != null ) {
            app.getMoveByIdAsync(move_id, new MyApp.Callback<Move>() {
                @Override
                public void onSuccess(Move result) {
                    mMove = result;
                    render( result );
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        else {
            mMove = new Move();
            render(mMove);
        }


        changeFragmentTo(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_delete, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menu_id = item.getItemId();
        MyApp app = (MyApp)getApplication();

        if( menu_id == R.id.menu_save ) {
            if( mMove == null )
                mMove = new Move();

            getValuesInto(mMove);

            app.saveAsync(mMove, new MyApp.Callback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    showSaved();
                }

                @Override
                public void onError(Exception e) {
                    showError(e);
                }
            });
            return true;
        }
        else if( menu_id == R.id.menu_delete ) {
            Utils.confirm(this, getString(R.string.event_delete_confirm), android.R.drawable.ic_dialog_alert, new Utils.ConfirmListener() {
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

        app.delAsync(mMove, new MyApp.CallbackSimple() {
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
        showMsg("Move Saved");
    }

    void showDeleted() {
        showMsg("Move Deleted");
    }

    void showError(Exception e) {
        showMsg(e.getMessage());
    }

    void getValuesInto(Move move) {
        float f = Formatters.parseAmount( mAmount.getText().toString() );
        move.amount = f;

        move.tag_id = mTagLabel.getText().toString();


        //event.dt_start = new Date();
        //event.dt_start = Date.
        //(mDtStart.getText().toString());
        //event.dt_end = mDtStart.getText().toString();
    }

    void render(Move move) {
        if( move == null )
            return;

        mAmount.setText(Formatters.renderAmount( move.amount ));
        mTagLabel.setText( move.tag_id );
    }

}
