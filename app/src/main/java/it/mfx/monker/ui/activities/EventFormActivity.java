package it.mfx.monker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.mfx.monker.MyApp;
import it.mfx.monker.R;
import it.mfx.monker.ui.Utils;
import it.mfx.monker.ui.fragments.DatePickerFragment;
import it.mfx.monker.ui.fragments.TimePickerFragment;
import it.mfx.monker.models.Event;

public class EventFormActivity extends AppCompatActivity implements DatePickerFragment.Listener, TimePickerFragment.Listener {

    public final static String PARM_EVENT_ID = "event_id";

    TextView mLabel;
    EditText mDtStart;
    EditText mDtEnd;

    Event mEvent;
    EditText currentTarget;

    private View.OnClickListener picker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentTarget = (EditText)v;
            if( currentTarget == null )
                return;

            String s = currentTarget.getText().toString();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
            try {
                cal.setTime(sdf.parse(s));
            }
            catch( ParseException e ) {
                //showError(e);
                e.printStackTrace();
                Log.e("EVENTFORM", e.getMessage());
            }

            showTimePickerDialog(cal);
            showDatePickerDialog(cal);
        }
    };

    private void closeForm() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSet(TimePicker view, int hourOfDay, int minute) {
        if( currentTarget == null )
            return;



        String val = currentTarget.getText().toString();
        val += " " + String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
        currentTarget.setText(val);
    }

    @Override
    public void onSet(DatePicker view, int year, int month, int day) {
        if( currentTarget == null )
            return;

        currentTarget.setText(year + "/" + String.format("%02d", (month + 1)) + "/" + String.format("%02d", day));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if( bar != null ) {
            bar.setDisplayHomeAsUpEnabled(true);
        }


        mLabel = findViewById(R.id.event_label);
        mDtStart = findViewById(R.id.event_dt_start);
        mDtEnd = findViewById(R.id.event_dt_end);

        MyApp app = (MyApp)getApplication();

        mDtStart.setOnClickListener(picker);
        mDtEnd.setOnClickListener(picker);

        String event_id = null;

        Intent intent = getIntent();
        if( intent != null ) {
            event_id = intent.getStringExtra(PARM_EVENT_ID);
        }
        else if( event_id != null ) {
            event_id = savedInstanceState.getParcelable(PARM_EVENT_ID);
        }

        if( event_id != null ) {
            app.getEventByIdAsync(event_id, new MyApp.Callback<Event>() {
                @Override
                public void onSuccess(Event result) {
                    mEvent = result;
                    render( result );
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        else {
            mEvent = new Event();
            render(mEvent);
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
            if( mEvent == null )
                mEvent = new Event();

            getValuesInto(mEvent);

            app.saveAsync(mEvent, new MyApp.Callback<Boolean>() {
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

    void deleteItem() {
        MyApp app = (MyApp)getApplication();

        app.delAsync(mEvent, new MyApp.CallbackSimple() {
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
        /*
        boolean isUiThread = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? Looper.getMainLooper().isCurrentThread()
                : Thread.currentThread() == Looper.getMainLooper().getThread();

        //And, if you wish to run something on the ui thread, you can use this:

        if( isUiThread ) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        else {

            final Context ctx = this;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //this runs on the ui thread
                    Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
        */
    }

    void showSaved() {
        showMsg("Event Saved");
    }

    void showDeleted() {
        showMsg("Event Deleted");
    }

    void showError(Exception e) {
        showMsg(e.getMessage());
    }

    void getValuesInto(Event event) {
        event.label = mLabel.getText().toString();
        //event.dt_start = new Date();
        //event.dt_start = Date.
        //(mDtStart.getText().toString());
        //event.dt_end = mDtStart.getText().toString();
    }

    void render(Event event) {
        String label = event.label;
        if( label == null )
            label = "";

        mLabel.setText(label);
    }

    //
    // DateTime Part
    //

    public void showDatePickerDialog(Calendar calendar) {

        DialogFragment newFragment = DatePickerFragment.newInstance(calendar);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void showTimePickerDialog(Calendar calendar) {
        DialogFragment newFragment = TimePickerFragment.newInstance(calendar);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}
