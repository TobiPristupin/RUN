package com.example.tobias.run;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * Object to represent a new tracked run for storage in database. Handles conversion between
 * the data retrieved directly from editor activity fields to the appropiate format and
 * datatype for storage in the db.
 */
public class TrackedRun {

    private int _distance;
    private long _time;
    private long _date;
    private int _rating;
    private String _unit;

    /**
     * Values will be passed as gathered directly from editor activity fields (Strings), and will be converted
     * for storage appropiately.
     */
    public TrackedRun(String distance, String time, String date, String rating, String unit){
        this._distance = formatDistance(distance);
        this._time = formatTime(time);
        this._date = formatDate(date);
        this._rating = Integer.valueOf(rating);
        this._unit = unit;
    }

    private int formatDistance(String distance){
        distance = distance.replace(",", "");
        return Integer.valueOf(distance) * 100; //Convert to metres
    }

    /**
     *
     * @param time String value in the format of 00:00:00
     * @return long time value in milliseconds
     *
     * Parses String time value and converts it into milliseconds.
     */
    private long formatTime(String time){
        String[] timeUnits = new String[3];
        //Split time into hours seconds minutes
        timeUnits = time.split(":");
        Period timePeriod = new Period()
                .withHours(Integer.valueOf(timeUnits[0]))
                .withMinutes(Integer.valueOf(timeUnits[1]))
                .withSeconds(Integer.valueOf(timeUnits[2]));
        //Return time in millis
        return timePeriod.toStandardDuration().getMillis();
    }

    /**
     * @param date String value in the format of (three letter day), (day of month), (month), (year),
     *             such as Wed, 2, 3, 2017
     * @return long date value in unix timestamp format
     *
     * Parses String date value and converts it into unix timestamp.
     */
    private long formatDate(String date){
        //Remove irrelevant day information
        date = date.split(",")[1].trim();
        String[] dateUnits = new String[3];
        //Split into year month day
        dateUnits = date.split("/");
        DateTime dateTime = new DateTime()
                .withYear(Integer.valueOf(dateUnits[2]))
                .withMonthOfYear(Integer.valueOf(dateUnits[1]))
                .withDayOfMonth(Integer.valueOf(dateUnits[0]));
        //Return time in seconds since epoch
        return dateTime.getMillis() / 1000;
    }

    public int getDistance(){
        return _distance;
    }

    public long getTime(){
        return _time;
    }

    public long getDate(){
        return _date;
    }

    public int getRating(){
        return _rating;
    }

    public String getUnit(){
        return _unit;
    }

}
