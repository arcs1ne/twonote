package com.onenote.twonote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.text.*;
import java.util.Timer;
import java.util.TimerTask;

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
        final TextView timerView = getActivity().findViewById(R.id.timerView);
        TextView eventView = getActivity().findViewById(R.id.eventView); //event name
        TextView subjView = getActivity().findViewById(R.id.subjView); //subject name
        final Event e = Event.correlate(new Date());
        if (e==null) return;
        Date deadline = e.getEndDate();

        eventView.setText(e.getName());
        subjView.setText(e.getTopic().getName());

        //code for timer thing
        //your code here
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (getActivity()==null) {
                    timer.cancel();
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        if (e.getEndDate().before(new Date())) {
                            timer.cancel();
                        } else {
                            long diff = e.getEndDate().getTime() - (new Date().getTime());
                            System.out.println(diff);
                            int seconds = (int) (diff / 1000) % 60;
                            int minutes = (int) ((diff / (1000 * 60)) % 60);
                            int hours = (int) ((diff / (1000 * 60 * 60)) % 24);
                            int days = (int) ((diff / (1000 * 60 * 60 * 24)));
                            timerView.setText(String.format("%02d:%02d:%02d",hours,minutes,seconds));
                        }
                    }
                });
            }
        },0,1000);
    }
}
