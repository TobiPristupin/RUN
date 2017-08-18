package com.example.tobias.run.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Object to represent a new tracked run for storage in database. Handles conversion between
 * the data retrieved directly from editor activity fields to the appropriate format and
 * datatype for storage in the db.
 */
public class TrackedRun implements Parcelable {
    //TODO: Remove ID field

    private float mDistanceKilometres;
    private float mDistanceMiles;
    private long mTime;
    private long mDate;
    private int mRating;

    private String mID = null;

    public TrackedRun(int id, long date, float distanceKilometres, float distanceMiles, long time, int rating, String unit){
        this.mDistanceKilometres = distanceKilometres;
        this.mDistanceMiles = distanceMiles;
        this.mTime = time;
        this.mDate = date;
        this.mRating = rating;

    }

    public TrackedRun(){
        // Default constructor required for firebase calls to DataSnapshot.getValue(User.class)

    }

    public TrackedRun(Parcel in) {
        this.mID = in.readString();
        this.mDate = in.readLong();
        this.mDistanceKilometres = in.readFloat();
        this.mTime = in.readLong();
        this.mRating = in.readInt();
        this.mDistanceMiles = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mID);
        parcel.writeLong(mDate);
        parcel.writeFloat(mDistanceKilometres);
        parcel.writeLong(mTime);
        parcel.writeInt(mRating);
        parcel.writeFloat(mDistanceMiles);
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

    public float getDistanceKilometres(){
        return mDistanceKilometres;
    }
    public float getDistanceMiles(){
        return mDistanceMiles;
    }
    public long getTime(){
        return mTime;
    }
    public long getDate(){ return mDate; }
    public int getRating(){
        return mRating;
    }
    public String getId(){ return mID; }

    public void setDistanceKilometres(float distanceKilometres){
        mDistanceKilometres = distanceKilometres;
    }
    public void setDistanceMiles(float distanceMiles){
        mDistanceMiles = distanceMiles;
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
    public void setId(String pushKey){
        mID = pushKey;
    }


}
