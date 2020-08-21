package com.onenote.twonote;

import java.sql.Time;
import java.util.*;

public class Timetable {
    protected Topic[][] week;
    protected static final double numHoursADay=24.0;

    public Timetable(){
        week=new Topic[7][2880];
    }

    public Topic[][] getWeek() {
        Topic[][] copy=new Topic[7][2880];
        Topic[] copy2=new Topic[2880];
        for (int i=0; i<7; i++){
            copy2=new Topic[2880];
            for (int j=0; j<2880; j++){
                copy2[j]=week[i][j];
            }
            copy[i]=copy2;
        }
        return copy;
    }

    public void addTopic(String eventName, int day, String startHour, int durationH, int durationM){//Note: 1=Mon, 2=Tues, ..., 7=Sun
        if (day>=1 && day<=7){
            int currDay=day;
            try{
                int n=Integer.parseInt(startHour);
                if (n>2400 || n<0 || startHour.length()>4){
                    throw new NumberFormatException();
                }
                else {
                    int hour=Integer.parseInt(startHour.substring(0, 2));
                    int minute=Integer.parseInt(startHour.substring(2));
                    int startM=hour*60+minute;
                    int endM=hour*60+minute+durationH*60+durationM;
                    int minutesLeft=durationH*60+durationM;
                    if (week[day][startM]!=null && week[day][endM]!=null){
                        try {
                            for (int i=startM; i<endM; i++){
                                if (week[day][i] != null) {
                                    System.out.println("Not Free");
                                    return;
                                }
                                else{
                                    minutesLeft-=1;
                                }
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException ex){
                            currDay+=1;
                            for (int j=0; j<minutesLeft; j++){
                                if (week[day][j] != null) {
                                    System.out.println("Not Free");
                                    return;
                                }
                            }
                        }
                        currDay-=1;
                        Topic topic=new Topic(eventName, "", new ArrayList<Event>());
                        try {
                            for (int i=startM; i<endM; i++){
                                if (week[day][i] == null) {
                                    week[day][i]=(topic);
                                }
                                else{
                                    minutesLeft-=1;
                                }
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException ex){
                            day+=1;
                            for (int j=0; j<minutesLeft; j++){
                                if (week[day][j] != null) {
                                    week[day][j]=(topic);
                                }
                            }
                        }
                    }
                }
            }
            catch (NumberFormatException ex){
                System.out.println(ex.getMessage());
                System.out.println("Invalid starting hour! Please input in a 24 hour format, such as 0630.");
            }
        }
    }
}