package it.mfx.monker.UI.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    public interface Listener {
        void onSet(TimePicker view, int hourOfDay, int minute);
    }

    private Listener mListener = null;

    public static TimePickerFragment newInstance(Calendar c) {
        TimePickerFragment f = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("hour",  c.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", c.get(Calendar.MINUTE));
        //args.putInt("second", c.get(Calendar.SECOND));
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        final Calendar c = Calendar.getInstance();
        int hour = args.getInt("hour", c.get(Calendar.HOUR_OF_DAY));
        int minute = args.getInt("minute", c.get(Calendar.MINUTE));
        //int second = args.getInt("second", c.get(Calendar.SECOND));


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if( context instanceof DatePickerFragment.Listener) {
            mListener = (Listener)context;
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if( mListener != null )
            mListener.onSet(view, hourOfDay, minute );
    }


}