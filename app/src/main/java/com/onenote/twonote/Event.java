package com.onenote.twonote;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Event implements Comparable{
    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

    protected String name;
    protected String description;
    protected int duration;
    protected Date date = new Date();
    protected Topic topic;
    protected static final SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static Map<String,Event> eventMap = new HashMap<>();
    public static Set<Event> eventArrayList = new TreeSet<>();

    public Event(String name, String description, Topic topic, String date, int duration){
        this.name=name;
        this.description=description;
        this.duration = duration; // in minutes
        this.topic = topic;
        try{
            this.date=dateFormatter.parse(date);
        }
        catch (ParseException ex){
            System.out.println(ex.getMessage());
        }
        addEvent();
    }
    public Event(String name, String description, Topic topic){
        this.name=name;
        this.description=description;
        this.topic = topic;
        this.date=new Date();
        addEvent();
    }
    public Event(String name, Topic topic){
        this.name=name;
        this.description="No description available";
        this.topic = topic;
        this.date=new Date();
        addEvent();
    }
    public Event(Event event){
        this.name=event.getName();
        this.description=event.getDescription();
        this.topic = event.topic;
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
    public Topic getTopic() { return topic; }
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
    public Date getEndDate() {
        return new Date(date.getTime()+TimeUnit.SECONDS.toMillis(duration * ONE_MINUTE_IN_MILLIS));
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
    public void setTopic(Topic topic) { this.topic = topic; }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Event){
            if (((Event) o).getUnformattedDate().after(this.date)){
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
        return "Event name: "+name+"\nEvent description: "+description+"\nEvent topic: "+topic.name
                +"\nEvent date: "+dateFormatter.format(date)+"\nEvent duration:" + duration;
    }

    /*public static ArrayList<Event> initArrayList() {
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

                Event e = new Event(name, desc, null, dt, dur);
                eventArrayList.add(e);
                eventMap.put(e.name,e);
                if (!eventTopicMap.containsKey(e.topic.name)) eventTopicMap.put(e.topic.name,new ArrayList<Event>());
                eventTopicMap.get(e.topic.name).add(e);
            }
//            Collections.sort(eventArrayList);
        }
        return eventArrayList;
    }*/

    public void addEvent(){
        eventArrayList.add(this);
        eventMap.put(name,this);
        //if (topic==null) return;
        topic.addEvent(this);

    }
    public static Set<Event> getEventArrayList(){
        return eventArrayList;
    }

    public static Event findEvent(String name) {
        return eventMap.get(name);
    }

    public static Event correlate(Date d){
        Set<Event> e = eventArrayList;
        Date starttime, endtime;
        int minutes;
        for (Event event:e){
            starttime = event.getUnformattedDate();
            endtime = event.getEndDate();
            if (endtime.compareTo(d) > 0 && starttime.compareTo(d) < 0){
                return event;
            }
        }
        return null;
    }
}
