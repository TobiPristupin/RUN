package com.example.tobias.run;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object to represent a new tracked run for storage in database. Handles conversion between
 * the data retrieved directly from editor activity fields to the appropiate format and
 * datatype for storage in the db.
 */
public class TrackedRun implements Parcelable {

    private double _distance;
    private long _time;
    private long _date;
    private int _rating;
    private String _unit;
    private Integer _id = null;

    /**
     * Values will be passed as gathered directly from editor activity fields (Strings), and will be converted
     * for storage appropriately.
     */
    public TrackedRun(String distance, String time, String date, String rating, String unit){
        this._distance = DateManager.distanceToDouble(distance);
        this._time = DateManager.timeToUnix(time);
        this._date = DateManager.dateToUnix(date);
        this._rating = Integer.valueOf(rating);
        this._unit = unit;
    }

    /**
     *
     * Values will be passed already formatted previously, as retrieved from database.
     */
    public TrackedRun(int id, long date, double distance, long time, int rating, String unit){
        this._distance = distance;
        this._time = time;
        this._date = date;
        this._rating = rating;
        this._unit = unit;
        //Id given by database.
        this._id = id;
    }

    public TrackedRun(){

    }

    public TrackedRun(Parcel in) {
        this._id = in.readInt();
        this._date = in.readLong();
        this._distance = in.readDouble();
        this._time = in.readLong();
        this._rating = in.readInt();
        this._unit = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(_id);
        parcel.writeLong(_date);
        parcel.writeDouble(_distance);
        parcel.writeLong(_time);
        parcel.writeInt(_rating);
        parcel.writeString(_unit);
    }

    public static final Parcelable.Creator<TrackedRun> CREATOR = new Parcelable.Creator<TrackedRun>(){

        @Override
        public TrackedRun createFromParcel(Parcel parcel) {
            return new TrackedRun(parcel);
        }

        @Override
        public TrackedRun[] newArray(int i) {
            return new TrackedRun[i];
        }


    };



    public double getDistance(){
        return _distance;
    }

    public long getTime(){
        return _time;
    }

    public long getDate(){ return _date; }

    public int getRating(){
        return _rating;
    }

    public String getUnit(){
        return _unit;
    }

    public Integer getId(){ return _id; }

    public void setDistance(String distance){

        _distance = DateManager.distanceToDouble(distance);
    }

    public void setTime(String time){
        _time = DateManager.timeToUnix(time);
    }

    public void setDate(String date){
        _date = DateManager.dateToUnix(date);
    }

    public void setRating(String rating){
        _rating = Integer.valueOf(rating);
    }

    public void setUnit(String unit){
        _unit = unit;
    }


}
