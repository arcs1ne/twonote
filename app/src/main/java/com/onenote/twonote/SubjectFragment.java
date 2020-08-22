package com.onenote.twonote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_OK;

public class SubjectFragment extends Fragment {
    private Topic current;
    private FloatingActionButton fab2, fab3;
    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
    private Uri photoURI;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subject, container, false);
        fab2 = v.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("topic",current!=null?current.name:null);
                DialogFragment newFragment = (current==null?new SubjectDialog():new CalendarDialog());
                newFragment.setArguments(b);
                newFragment.show(getActivity().getSupportFragmentManager(), "Add topic");
            }
        });
        fab3 = v.findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Map<Topic, List<Event>> eByTopic = new HashMap<>();
        List<Topic> topics = Topic.getTopicArrayList();
        //topics.add(new Topic("Chem","Chemistry :("));
        //eByTopic.put(topics.get(0), Arrays.asList(new Event("ICA",)));
        String c = getArguments()==null?null:(String)(getArguments().get("topic"));
        System.out.println(c);

        current = Topic.findTopic(c);

        LinearLayout ll = getActivity().findViewById(R.id.linearLayout);
        ll.removeAllViews();

        if (current!=null) {
            for (final Event e : current.events) {
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
                        viewEvent(e.name);
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
                        current=t;
                        refresh();
                    }
                });
            }
        }
    }

    public void refresh() {
        Bundle b = new Bundle();
        b.putString("topic",current.name);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        this.setArguments(b);
        ft.addToBackStack("die");
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

    private void viewEvent(String e) {
        Bundle b = new Bundle();
        b.putString("event",e);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment f = new EventFragment();
        f.setArguments(b);
        ft.replace(R.id.nav_host_fragment,f).addToBackStack("View Event");
        ft.commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
        switch (requestCode) {
            case 3:
                if (resultCode == RESULT_OK) {
                    try {
                        System.out.println(data);
                        photoURI = data.getData();
                        Event ee = correlate(photoURI);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    public Event correlate(Uri photoURI) {
        ExifInterface intf = null;
        try (InputStream inputStream = getContext().getContentResolver().openInputStream(photoURI)) {
            intf = new ExifInterface(inputStream);
        } catch(IOException exc) {
            exc.printStackTrace();
        }

        if(intf == null) {
            Log.d("TAG","file not found");
        }
        if (intf.getAttribute(ExifInterface.TAG_DATETIME)==null) {
            Toast.makeText(getContext(),"Date/time information not found!",Toast.LENGTH_LONG);

            return null;
        }
        String dateString = intf.getAttribute(ExifInterface.TAG_DATETIME);

        String[] str = dateString.split(" ");
        Date properDate = convert(dateString);

        return Event.correlate(properDate);
    }
    public static Date convert(String EXIF_TAG_DATETIME){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);

        try {
            return simpleDateFormat.parse(EXIF_TAG_DATETIME);
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }


}

