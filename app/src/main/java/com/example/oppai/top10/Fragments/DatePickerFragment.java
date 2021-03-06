package com.example.oppai.top10.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Oppai on 7/19/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private org.joda.time.LocalDate DateWanted;
    private Button button;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateWanted = new org.joda.time.LocalDate(year,month + 1,dayOfMonth);
        assert this.getArguments() != null;
        button = getActivity().findViewById((Integer) this.getArguments().get("Id"));
        button.setText(DateWanted.toString());


    }





}
