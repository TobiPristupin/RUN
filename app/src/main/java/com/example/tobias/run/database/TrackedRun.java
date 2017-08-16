package com.example.tobias.run.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tobias.run.utils.DateManager;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Object to represent a new tracked run for storage in database. Handles conversion between
 * the data retrieved directly from editor activity fields to the appropriate format and
 * datatype for storage in the db.
 */
public class TrackedRun implements Parcelable {
    //TODO: Remove ID field

    private float mDistance;
    private long mTime;
    private long mDate;
    private int mRating;
    private String mUnit;
    private Integer mID = null;

    public TrackedRun(int id, long date, float distance, long time, int rating, String unit){
        this.mDistance = distance;
        this.mTime = time;
        this.mDate = date;
        this.mRating = rating;
        this.mUnit = unit;
        this.mID = id;
    }


    public TrackedRun(){
        // Default constructor required for firebase calls to DataSnapshot.getValue(User.class)

    }

    public TrackedRun(Parcel in) {
        this.mID = in.readInt();
        this.mDate = in.readLong();
        this.mDistance = in.readFloat();
        this.mTime = in.readLong();
        this.mRating = in.readInt();
        this.mUnit = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeLong(mDate);
        parcel.writeFloat(mDistance);
        parcel.writeLong(mTime);
        parcel.writeInt(mRating);
        parcel.writeString(mUnit);
    }

    @Exclude
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

    public float getDistance(){
        return mDistance;
    }
    public long getTime(){
        return mTime;
    }
    public long getDate(){ return mDate; }
    public int getRating(){
        return mRating;
    }
    public String getUnit(){
        return mUnit;
    }
    public Integer getId(){ return mID; }

    /*When passing unformatted values (straight from EditorActivity) call setter methods instead of constructor.
    These methods will convert the values to store them into the database. */
    public void setDistance(float distance){
        mDistance = distance;
    }
    public void setTime(long time){
        mTime = time;
    }
    public void setDate(long date){
        mDate = date;
    }
    public void setRating(int rating){
        mRating = rating;
    }
    public void setUnit(String unit){
        mUnit = unit;
    }


}
