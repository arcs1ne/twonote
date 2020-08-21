package com.onenote.twonote;

import java.text.ParseException;
import java.util.Date;

public class RecurringEvent extends Event{
    protected int numRecurrence;

    public RecurringEvent(String name, String description, String date, int duration, int numRecurrence){
        super(name, description, date, duration);
        this.numRecurrence=numRecurrence;
    }
    public RecurringEvent(String name, String description, int numRecurrence){
        super(name, description);
        this.numRecurrence=numRecurrence;
    }
    public RecurringEvent(String name, int numRecurrence){
        super(name);
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
