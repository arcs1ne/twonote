package com.onenote.twonote;

import java.util.*;

public class Topic {
    protected String name;
    protected String description;
    protected ArrayList<Event> events;

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
}
