package com.onenote.twonote;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EventFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Event e = new Event("ICA","Chem assignment or something lol");

        ((TextView)getActivity().findViewById(R.id.eventView)).setText(e.getName());
        ((TextView)getActivity().findViewById(R.id.descView)).setText(e.getDescription());
        ((TextView)getActivity().findViewById(R.id.dateView)).setText(e.getDate());
        ((TextView)getActivity().findViewById(R.id.topicView)).setText("pretend Chem");
    }
}
