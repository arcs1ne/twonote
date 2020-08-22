package com.onenote.twonote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.text.*;

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
        long diff=1;
        try {
            while (diff>0) {
                diff = e.getEndDate().getTime() - (new Date().getTime());
                int seconds = (int) (diff / 1000) % 60;
                int minutes = (int) ((diff / (1000 * 60)) % 60);
                int hours = (int) ((diff / (1000 * 60 * 60)) % 24);
                int days = (int) ((diff / (1000 * 60 * 60 * 24)));
                timerView.setText(days+" days, "+hours+" hours, "+minutes+" minutes and "+seconds+" seconds left before event"+e.getName()+"ends");
                Thread.sleep(1000);
            }
        }
        catch (Exception ex){
            System.out.println(ex.getStackTrace());
        }
    }
}
