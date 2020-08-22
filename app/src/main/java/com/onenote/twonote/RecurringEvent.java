package com.onenote.twonote;

import java.text.ParseException;
import java.util.Date;

public class RecurringEvent extends Event{
    protected int numRecurrence;

    public RecurringEvent(String name, String description, Topic topic, String date, int duration, int numRecurrence){
        super(name, description, topic, date, duration);
        this.numRecurrence=numRecurrence;
    }
    public RecurringEvent(String name, String description, Topic topic, int numRecurrence){
        super(name, description, topic);
        this.numRecurrence=numRecurrence;
    }
    public RecurringEvent(String name, Topic topic, int numRecurrence){
        super(name, topic);
        this.numRecurrence=numRecurrence;
    }
    public RecurringEvent(Event event, int numRecurrence){
        super(event);
        this.numRecurrence=numRecurrence;
    }
    public RecurringEvent(RecurringEvent event){
        super(event);
        this.numRecurrence=event.getNumRecurrence();
    }

    public int getNumRecurrence() {
        return numRecurrence;
    }
    public void setNumRecurrence(int numRecurrence){
        this.numRecurrence=numRecurrence;
    }

    public String toString(){
        return super.toString()+"\nNumber of recurrences: "+numRecurrence;
    }
}
