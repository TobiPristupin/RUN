package com.example.tobias.run;

import org.joda.time.DateTime;
import org.joda.time.Period;



/**
 * HistoryListItem class to hold data for custom ListView in History fragment
 */
public class HistoryListItem {

    private int distance;
    private Period time;
    private DateTime date;
    private int rating;
    private String unit;
    private int id;

    public HistoryListItem(int distance, Period time, DateTime date, int rating){
        this.distance = distance;
        this.time = time;
        this.date = date;
        this.rating = rating;
    }

    public int getDistance(){
        return distance;
    }

    public Period getTime(){
        return time;
    }

    public DateTime getDate() {
        return date;
    }

    public int getRating(){
        return rating;
    }


}
