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
    private boolean isFavorite;

    public HistoryListItem(int distance, Period time, DateTime date, boolean isFavorite){
        this.distance = distance;
        this.time = time;
        this.date = date;
        this.isFavorite = isFavorite;
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

    public boolean isFavorite(){
        return isFavorite;
    }


}
