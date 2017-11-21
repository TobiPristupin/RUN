package com.example.tobias.run.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tobias.run.utils.ConversionUtils;
import com.google.firebase.database.Exclude;

/**
 * Object to represent a new tracked run for storage in database. Handles conversion between
 * the data retrieved directly from editor activity fields to the appropriate format and
 * datatype for storage in the db.
 */
public class TrackedRun implements Parcelable {

    private float mDistanceKilometres;
    private float mDistanceMiles;
    private long mTime;
    private long mDate;
    private int mRating;
    private long mMilePace;
    private long mKilometrePace;
    private String mID = null;

    public TrackedRun(long date, float distanceKilometres, float distanceMiles, long time, int rating){
        this.mDistanceKilometres = distanceKilometres;
        this.mDistanceMiles = distanceMiles;
        this.mTime = time;
        this.mDate = date;
        this.mRating = rating;
        this.mMilePace = ConversionUtils.calculatePace(mDistanceMiles, mTime);
        this.mKilometrePace = ConversionUtils.calculatePace(mDistanceKilometres, mTime);
    }

    public TrackedRun(long date, float distanceKilometres, float distanceMiles, long time, int rating, String id){
        this.mDistanceKilometres = distanceKilometres;
        this.mDistanceMiles = distanceMiles;
        this.mTime = time;
        this.mDate = date;
        this.mRating = rating;
        this.mMilePace = ConversionUtils.calculatePace(mDistanceMiles, mTime);
        this.mKilometrePace = ConversionUtils.calculatePace(mDistanceKilometres, mTime);
        this.mID = id;
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
        this.mMilePace = in.readLong();
        this.mKilometrePace = in.readLong();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        
        if (obj instanceof TrackedRun == false){
            return false;
        }
            
        TrackedRun run = (TrackedRun) obj;

        if (!this.getId().equals(run.getId())) return false;
        if (this.getDate() != run.getDate()) return false;
        if (this.getDistanceKilometres() != run.getDistanceKilometres()) return false;
        if (this.getRating() != run.getRating()) return false;
        if (this.getTime() != run.getTime()) return false;
        //No need to check for mile or km pace because pace is computed off time and distance.

        return true;
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
        parcel.writeLong(mMilePace);
        parcel.writeLong(mKilometrePace);
    }

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

    public long getMilePace(){
        return mMilePace;
    }

    public long getKmPace() {
        return mKilometrePace;
    }

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

    public void setMilePace(long milePace){
        mMilePace = milePace;
    }

    public void setKmPace(long kmPace) {
        mKilometrePace = kmPace;
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

}
