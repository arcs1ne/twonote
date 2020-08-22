package com.onenote.twonote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FocusFragment extends Fragment {
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_focus, container, false);
            return v;
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView timerView = getActivity().findViewById(R.id.timerView);
        TextView eventView = getActivity().findViewById(R.id.eventView); //event name
        TextView subjView = getActivity().findViewById(R.id.subjView); //subject name
        Event e = Event.correlate(new Date());
        Date deadline = e.getEndDate();

        eventView.setText(e.getName());
        subjView.setText(e.getTopic().getName());

        //your code here

        // timerView.setText(...);
    }
}
