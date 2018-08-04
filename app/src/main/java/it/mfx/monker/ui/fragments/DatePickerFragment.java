package it.mfx.monker.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerFragment  extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    public interface Listener {
        void onSet(DatePicker view, int year, int month, int day);
    }

    private Listener mListener = null;

    public static DatePickerFragment newInstance(Calendar c) {
        DatePickerFragment f = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", c.get(Calendar.YEAR));
        args.putInt("month", c.get(Calendar.MONTH));
        args.putInt("day", c.get(Calendar.DAY_OF_MONTH));
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final Calendar c = Calendar.getInstance();
        int year = args.getInt("year", c.get(Calendar.YEAR));
        int month = args.getInt("month", c.get(Calendar.MONTH));
        int day = args.getInt("day", c.get(Calendar.DAY_OF_MONTH));


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if( context instanceof Listener ) {
            mListener = (Listener)context;
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if( mListener != null )
            mListener.onSet(view, year, month, day );
        //DateEdit.setText(day + "/" + (month + 1) + "/" + year);
    }
}
