package com.onenote.twonote;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CalendarFragment extends Fragment {
    private DatePicker calendar;
    private TextView picked;
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendar = v.findViewById(R.id.calendarView);
        picked = v.findViewById(R.id.pickedDate);
        fab = v.findViewById(R.id.fab);
        calendar.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Log.d("TAG", i + "," + i1 + "," + i2);
                picked.setText(i2 + "/" + (i1 + 1) + "/" + i);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new CalendarDialog();
                newFragment.show(getActivity().getSupportFragmentManager(), "Add event");
            }
        });
        return v;
    }
}


