package com.onenote.twonote;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SubjectFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subject, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<Topic, List<Event>> eByTopic = new HashMap<>();
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("Chem","Chemistry :("));
        eByTopic.put(topics.get(0), Arrays.asList(new Event("ICA","chem??","26/07/2004 00:00",30)));

        String c = getArguments()==null?null:(String)(getArguments().get("topic"));
        System.out.println(c);
        Topic current=null;
        for (Topic t: topics) {
            if (t.getName().equals(c)) {
                current=t;
            }
        }

        LinearLayout ll = getActivity().findViewById(R.id.linearLayout);
        ll.removeAllViews();

        if (current!=null) {
            for (final Event e : eByTopic.get(current)) {
                LinearLayout cl = makeCardView(ll, getContext());
                TextView name = new TextView(getContext());
                TextView desc = new TextView(getContext());
                name.setText(e.name);
                name.setTextSize(24);
                desc.setText(e.description+"\n"+5+" records.");
                cl.addView(name);
                cl.addView(desc);
                ((CardView)cl.getParent()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewEvent(e);
                    }
                });
            }
        } else {
            for (final Topic t : topics) {
                LinearLayout cl = makeCardView(ll, getContext());
                TextView name = new TextView(getContext());
                TextView desc = new TextView(getContext());
                name.setText(t.name);
                name.setTextSize(24);
                desc.setText(t.description+"\n"+t.events.size()+" records.");
                cl.addView(name);
                cl.addView(desc);
                ((CardView)cl.getParent()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reloadFragment(t);
                    }
                });
            }
        }
    }

    private void reloadFragment(Topic t) {
        Bundle b = new Bundle();
        b.putString("topic",t.name);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(this,"events");
        this.setArguments(b);
        ft.commit();
    }

    private static LinearLayout makeCardView(LinearLayout ll, Context ctx) {
        CardView c = new CardView(ctx);
        c.setClickable(true);
        TypedValue o = new TypedValue();
        ctx.getTheme().resolveAttribute(android.R.attr.selectableItemBackground,o,true);
        c.setForeground(ContextCompat.getDrawable(ctx,o.resourceId));
        LinearLayout cl = new LinearLayout(ctx);
        cl.setPadding(16, 16, 16, 16);
        cl.setOrientation(LinearLayout.VERTICAL);
        c.addView(cl);
        ll.addView(c);
        return cl;
    }

    private void viewEvent(Event e) {
        Bundle b = new Bundle();
        b.putString("event",e.name);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,new EventFragment());
        this.setArguments(b);
        ft.commit();
    }
}

