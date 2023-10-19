package com.slinger.bodygoals.ui.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    MutableLiveData<Date> selectedDateLiveData = new MutableLiveData<>(Calendar.getInstance().getTime());

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (getActivity() == null)
            throw new IllegalStateException("Activity should not be 'null'.");

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, dayOfMonth);

        selectedDateLiveData.setValue(calendar.getTime());
    }

    public LiveData<Date> getSelectedDateLiveData() {
        return selectedDateLiveData;
    }
}