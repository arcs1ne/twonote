package com.onenote.twonote;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import javax.security.auth.Subject;

public class SubjectDialog extends DialogFragment{
    private Button addevent;
    private EditText eventEdit;
    static ArrayList<Topic> topicArrayList = Topic.getTopicArrayList();
    private ArrayList<Event> tobeadded = new ArrayList<>();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View content =  inflater.inflate(R.layout.subject_dialog, null);
        addevent = content.findViewById(R.id.addnewevent);
        eventEdit = content.findViewById(R.id.eventname);
        eventEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (eventEdit.getText().toString().trim() != null)
                    addevent.setEnabled(true);
                else
                    addevent.setEnabled(false);
            }
        });
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tobeadded.add(new Event(eventEdit.getText().toString()));
                eventEdit.setText("");
                addevent.setEnabled(false);
                Toast.makeText(getContext(),"Event added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(content)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    int u = 0;
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText a = SubjectDialog.this.getDialog().findViewById(R.id.name);
                        EditText b = SubjectDialog.this.getDialog().findViewById(R.id.desc);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> field = new HashMap<String, Object>();
                        String mname = a.getText().toString().trim();
                        String mdesc = b.getText().toString().trim();
                        field.put("name", mname);
                        field.put("description",mdesc);
                        for (Event k: tobeadded){
                            u++;
                            field.put("Event " + u, k);
                        }
                        Log.d("field", field+"");
                        Topic.addToArrayList(topicArrayList, new Topic(mname,mdesc,tobeadded));

                        db.collection("topics").document(mname).set(field);
                        Toast.makeText(getContext(), "Successfully saved topic into system!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SubjectDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

