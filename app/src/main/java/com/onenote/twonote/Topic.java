package com.onenote.twonote;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;

public class Topic {
    protected String name;
    protected String description;
    protected ArrayList<Event> events;
    public static ArrayList<Topic> topicArrayList = initArrayList();
    public Topic(String name, String description, Event... events){
        this.name=name;
        this.events=new ArrayList<>();
        addAllEvents(events);
        this.description=description;
    }
    public Topic(String name, Event... events){
        this(name, "No description available", events);
    }
    public Topic(String name, ArrayList<Event> events){
        this(name, "No description available", events);
    }
    public Topic (String name, String description, ArrayList<Event> events){
        this.name=name;
        this.events=new ArrayList<>();
        addAllEvents(events);
        this.description=description;
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
        ArrayList<Topic> topicArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> topicTask = db.collection("topics").get();
        while(!topicTask.isComplete()){}
        if(topicTask.isSuccessful()) {
            QuerySnapshot snapshot = topicTask.getResult();
            for (QueryDocumentSnapshot doc : snapshot) {
                String name = doc.get("name", String.class);
                String desc = doc.get("description", String.class);
                topicArrayList.add(new Topic(name, desc));
            }
        }
        return topicArrayList;
    }
    public static ArrayList<Topic> addToArrayList(ArrayList<Topic> topicArrayList1, Topic topic){
        topicArrayList1.add(topic);
        topicArrayList = topicArrayList1;
        return topicArrayList;
    }
    public static ArrayList<Topic> getTopicArrayList(){
        return topicArrayList;
    }
}
