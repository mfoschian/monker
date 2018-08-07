package it.mfx.monker.ui.activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import it.mfx.monker.R;
import it.mfx.monker.models.Tag;
import it.mfx.monker.ui.fragments.TagChooserFragment;

public class MoveActivity extends AppCompatActivity implements TagChooserFragment.Listener {


    /*
    void backToPreviousFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        int stack_size = fragmentManager.getBackStackEntryCount();
        if( stack_size > 1 )
            fragmentManager.popBackStack();
    }
    */


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
            Log.i("TEST","Choosed tag " + tag.label);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_form);

        changeFragmentTo(null);
    }
}
