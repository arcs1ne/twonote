package com.onenote.twonote;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarDialog extends DialogFragment{
    static ArrayList<Event> eventArrayList = Event.getEventArrayList();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View content =  inflater.inflate(R.layout.calendar_dialog, null);
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
                        field.put("name", mname);
                        field.put("duration", mdur);
                        field.put("description",mdesc);
                        field.put("date and time", mdt);
                        Log.d("field", field+"");

                        Event.addToArrayList(eventArrayList, new Event(mname,mdesc,mdt,mdur));

                        db.collection("events").document(mname).set(field);
                        Toast.makeText(getContext(), "Successfully saved event into system!", Toast.LENGTH_SHORT).show();
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

