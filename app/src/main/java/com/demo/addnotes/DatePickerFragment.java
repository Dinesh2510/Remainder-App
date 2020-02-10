package com.demo.addnotes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;



import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    OnDateSetListener mDateSetListener;
    DatePickerDialog datePickerDialog;
    public static Calendar mCalendar = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d("CALENDAR_DATA", "Called");

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), R.style.datepicker, this, yy, mm, dd);


        if (mCalendar != null) {
            datePickerDialog.getDatePicker().setMinDate(mCalendar.getTimeInMillis());
        } else {
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        }
        return datePickerDialog;
    }


    public void setMinDate(int date, int month, int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        mCalendar = calendar;
    }

    public void setOnDateSetListener(OnDateSetListener dateSetListener) {
        this.mDateSetListener = dateSetListener;

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (this.mDateSetListener != null) {
            this.mDateSetListener.onDateSet(view, year, month, dayOfMonth);


        }
    }


    public interface OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

}