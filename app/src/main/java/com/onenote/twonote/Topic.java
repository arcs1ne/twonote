package com.onenote.twonote;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;

import androidx.annotation.NonNull;

public class Topic {
    protected String name;
    protected String description;
    protected ArrayList<Event> events;
    private static Map<String,Topic> topicMap = new HashMap<>();
    public static ArrayList<Topic> topicArrayList = initArrayList();
    public Topic(String name, String description, Event... events){
        this.name=name;
        this.events=new ArrayList<>();
        addAllEvents(events);
        this.description=description;
        addTopic();
    }
    public Topic(String name, Event... events){
        this(name, "No description available", events);
        addTopic();
    }
    public Topic(String name, ArrayList<Event> events){
        this(name, "No description available", events);
        addTopic();
    }
    public Topic (String name, String description, ArrayList<Event> events){
        this.name=name;
        this.events=new ArrayList<>();
        addAllEvents(events);
        this.description=description;
        addTopic();
    }
    public Topic(Topic topic){
        this(topic.getName(), topic.getDescription(), topic.getEvents());
    }

    public String getName(){
        return name;
    }
    public ArrayList<Event> getEvents(){
        ArrayList<Event> copy=new ArrayList<>();
        for (Event e : events){
            copy.add(new Event(e));
        }
        return copy;
    }
    public String getDescription(){
        return description;
    }

    public void setName(String name){
        this.name=name;
    }
    public void addEvent(Event event){
        events.add(event);
        System.out.println(events);
    }
    public void addAllEvents(Event... events){
        for (Event e : events){
            this.events.add(new Event(e));
        }
    }
    public void addAllEvents(ArrayList<Event> events){
        for (Event e : events){
            this.events.add(new Event(e));
        }
    }
    public void setDescription(String description){
        this.description=description;
    }

    public String toString(){
        String s="Topic name: "+name+"\nTopic description: "+description+"\nTopic events:\n";
        for (Event e : events){
            s+="\t"+(e.toString())+"\n";
        }
        return s;
    }
    public static ArrayList<Topic> initArrayList() {
        topicArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> topicTask = db.collection("topics").get();
        while(!topicTask.isComplete()){}
        if(topicTask.isSuccessful()) {
            QuerySnapshot snapshot = topicTask.getResult();
            for (final QueryDocumentSnapshot doc : snapshot) {
                String name = doc.get("name", String.class);
                String desc = doc.get("description", String.class);
                int i = 1;
                final ArrayList<Event> events = new ArrayList<>();
                final Topic to = new Topic(name, desc, events);
                while((Map) doc.get("Event " + i) != null){
                    final Map <String, String> t = (Map) doc.get("Event " + i);
                    DocumentReference dr = db.collection("events").document(t.get("name"));
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.isSuccessful()) return;
                            DocumentSnapshot d = task.getResult();
                            if (!d.exists()) return;
                            int dur = d.get("duration", Integer.class);
                            String name = d.get("name", String.class);
                            String desc = d.get("description", String.class);
                            String dt = d.get("date and time", String.class);
                            Event e = new Event(name, desc, to, dt, dur);
                            events.add(e);
                        }
                    });
                    i++;
                }
            }
        }
        return topicArrayList;
    }
    public void addTopic() {
        topicArrayList.add(this);
        topicMap.put(name,this);
    }
    public static Topic findTopic(String name) {
        return topicMap.get(name);
    }
    public static ArrayList<Topic> getTopicArrayList(){
        return topicArrayList;
    }
    public static Set<String> getTopicNameSet(){
        return topicMap.keySet();
    }
    public static ArrayList<Event> findEventsByTopic(Topic t) {
        return t.getEvents();
    }
    public static ArrayList<Event> findEventsByTopic(String t) {
        return findTopic(t).getEvents();
    }
}
