package com.example.tobias.run.data;

/**
 * Created by Tobi on 11/8/2017.
 */

public class PersonalRecord {

    private boolean mAchieved;
    private String mTitle;
    /*
    Decides if the important value of the record to be shown is distance, such as in longest run.
    If false, time will be used as the value to be shown.
     */
    private boolean showDistanceAsValue;
    private long mTime;
    private long mDate;
    private long mMilePace;
    private long mKilometerPace;
    private float mDistanceKilometers;
    private float mDistanceMiles;

    private PersonalRecord(Builder builder) {
        this.mAchieved = builder.mAchieved;
        this.mTitle = builder.mTitle;
        this.showDistanceAsValue = builder.showDistanceAsValue;
        this.mTime = builder.mTime;
        this.mDate = builder.mDate;
        this.mMilePace = builder.mMilePace;
        this.mKilometerPace = builder.mKilometerPace;
        this.mDistanceKilometers = builder.mDistanceKilometers;
        this.mDistanceMiles = builder.mDistanceMiles;
    }

    public boolean isAchieved() {
        return mAchieved;
    }

    public long getTime() {
        return mTime;
    }

    public long getDate() {
        return mDate;
    }

    public long getMilePace() {
        return mMilePace;
    }

    public long getKilometerPace() {
        return mKilometerPace;
    }

    public float getDistanceKilometers() {
        return mDistanceKilometers;
    }

    public float getDistanceMiles() {
        return mDistanceMiles;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean showDistanceAsValue() {
        return showDistanceAsValue;
    }



    public static class Builder {

        private boolean mAchieved;
        private String mTitle;
        private boolean showDistanceAsValue;
        private long mTime;
        private long mDate;
        private long mMilePace;
        private long mKilometerPace;
        private float mDistanceKilometers;
        private float mDistanceMiles;

        public Builder(boolean achieved, String title, boolean showDistanceAsValue){
            this.mAchieved = achieved;
            this.mTitle = title;
            this.showDistanceAsValue = showDistanceAsValue;
        }

        public Builder withTrackedRun(TrackedRun trackedRun){
            if (!mAchieved){
                throw new IllegalArgumentException("Cannot add TrackedRun data if record hasn't been achieved");
            }

            mDate = trackedRun.getDate();
            mTime = trackedRun.getTime();
            mMilePace = trackedRun.getMilePace();
            mKilometerPace = trackedRun.getKmPace();
            mDistanceKilometers = trackedRun.getDistanceKilometres();
            mDistanceMiles = trackedRun.getDistanceMiles();

            return this;
        }

        public PersonalRecord build(){
            return new PersonalRecord(this);
        }
    }
}
