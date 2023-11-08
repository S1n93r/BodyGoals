package com.slinger.bodygoals.ui.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    MutableLiveData<LocalDate> selectedDateLiveData = new MutableLiveData<>(LocalDate.now());

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LocalDate localDate = selectedDateLiveData.getValue();

        return new DatePickerDialog(getActivity(), this, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (getActivity() == null)
            throw new IllegalStateException("Activity should not be 'null'.");

        selectedDateLiveData.setValue(LocalDate.of(year, month + 1, dayOfMonth));
    }

    public LiveData<LocalDate> getSelectedDateLiveData() {
        return selectedDateLiveData;
    }
}