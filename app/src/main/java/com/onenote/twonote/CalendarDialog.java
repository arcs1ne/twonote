package com.onenote.twonote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CalendarDialog extends DialogFragment{
    static Set<Event> eventArrayList = Event.getEventArrayList();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View content =  inflater.inflate(R.layout.calendar_dialog, null);
        final Spinner topicSpinner = content.findViewById(R.id.topicSpinner);
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,Topic.getTopicNameSet().toArray());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topicSpinner.setAdapter(aa);
        builder.setView(content)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText a = CalendarDialog.this.getDialog().findViewById(R.id.name);
                        EditText b = CalendarDialog.this.getDialog().findViewById(R.id.dur);
                        EditText c = CalendarDialog.this.getDialog().findViewById(R.id.desc);
                        EditText d = CalendarDialog.this.getDialog().findViewById(R.id.datetime);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> field = new HashMap<String, Object>();
                        String mname = a.getText().toString().trim();
                        int mdur = Integer.parseInt(b.getText().toString().trim());
                        String mdesc = c.getText().toString().trim();
                        String mdt = d.getText().toString().trim();
                        Topic t = Topic.findTopic(topicSpinner.getSelectedItem().toString());
                        field.put("name", mname);
                        field.put("duration", mdur);
                        field.put("description",mdesc);
                        field.put("date and time", mdt);
                        Log.d("field", field+"");

                        new Event(mname,mdesc,t,mdt,mdur); //TODO

                        db.collection("events").document(mname).set(field);

                        Map<String,Object> f2 = new HashMap<>();
                        f2.put("Event "+t.getEvents().size(),field);
                        db.collection("topics").document(topicSpinner.getSelectedItem().toString()).update(f2);

                        Toast.makeText(getContext(), "Successfully saved event into system!", Toast.LENGTH_SHORT).show();
                        System.out.println(getParentFragment());
                        if (getParentFragment() instanceof SubjectFragment) {
                            ((SubjectFragment)getParentFragment()).refresh();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CalendarDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

