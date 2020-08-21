package com.onenote.twonote;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.*;
import java.util.*;

public class Event implements Comparable{
    protected String name;
    protected String description;
    protected int duration;
    protected Date date;
    protected static final SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static ArrayList<Event> eventArrayList = initArrayList();

    public Event(String name, String description, String date, int duration){
        this.name=name;
        this.description=description;
        this.duration = duration; // in minutes
        try{
            this.date=dateFormatter.parse(date);
        }
        catch (ParseException ex){
            System.out.println(ex.getMessage());
        }
    }
    public Event(String name, String description){
        this.name=name;
        this.description=description;
        this.date=new Date();
    }
    public Event(String name){
        this.name=name;
        this.description="No description available";
        this.date=new Date();
    }
    public Event(Event event){
        this.name=event.getName();
        this.description=event.getDescription();
        try{
            this.date=dateFormatter.parse(event.getDate());
        }
        catch (ParseException ex){
            System.out.println(ex.getMessage());
        }
    }

    public int getDuration() {
        return duration;
    }

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getDate(){
        return dateFormatter.format(date);
    }
    public Date getUnformattedDate(){
        return date;
    }
    public String getDate(String format){
        try {
            SimpleDateFormat tempFormatter = new SimpleDateFormat(format);
            return tempFormatter.format(date);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return "Failed to format date! Please check your format.";
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setName(String name){
        this.name=name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(String date){
        try{
            this.date=dateFormatter.parse(date);
        }
        catch (ParseException ex){
            System.out.println(ex.getMessage());
        }
    }
    public void setDate(Date date){
        this.date=date;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Event){
            if (((Event) o).getUnformattedDate().before(this.date)){
                return -1;
            }
            else if (((Event) o).getUnformattedDate().before(this.date)){
                return 1;
            }
            else {
                return 0;
            }
        }
        else{
            return -999;
        }
    }

    public String toString(){
        return "Event name: "+name+"\nEvent description: "+description
                +"\nEvent Date: "+dateFormatter.format(date)+"\nEvent Duration:" + duration;
    }

    public static ArrayList<Event> initArrayList() {
        ArrayList<Event> eventArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> eventTask = db.collection("events").get();
        while(!eventTask.isComplete()){}
        if(eventTask.isSuccessful()) {
            QuerySnapshot snapshot = eventTask.getResult();
            for (QueryDocumentSnapshot doc : snapshot) {
                int dur = doc.get("duration", Integer.class);
                String name = doc.get("name", String.class);
                String desc = doc.get("description", String.class);
                String dt = doc.get("date and time", String.class);
                eventArrayList.add(new Event(name, desc, dt, dur));
            }
            Collections.sort(eventArrayList);
        }
        return eventArrayList;
    }
    public static ArrayList<Event> addToArrayList(ArrayList<Event> eventArrayList1, Event ev){
        eventArrayList1.add(ev);
        eventArrayList = eventArrayList1;
        return eventArrayList1;
    }
    public static ArrayList<Event> getEventArrayList(){
        return eventArrayList;
    }
}
