package com.example.tobias.run.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.tobias.run.utils.RunUtils;
import com.google.firebase.database.Exclude;

import org.apache.commons.collections.Predicate;

/**
 * Object to represent a run.
 */
public class Run implements Parcelable, Comparable<Run> {

    private float distanceKilometres;
    private float distanceMiles;
    private long time;
    private long date;
    private int rating;
    private long milePace;
    private long kilometrePace;
    private String id = null;

    private Run(long date, float distanceKilometres, float distanceMiles, long time, int rating, long milePace, long kilometrePace){
        this.distanceKilometres = distanceKilometres;
        this.distanceMiles = distanceMiles;
        this.time = time;
        this.date = date;
        this.rating = rating;
        this.milePace = milePace;
        this.kilometrePace = kilometrePace;
    }

    public static Run withKilometers(long date, float distanceKilometres, long time, int rating){
        float distanceMiles = RunUtils.kilometresToMiles(distanceKilometres);
        long kmPace = RunUtils.calculatePace(distanceKilometres, time);
        long milePace = RunUtils.calculatePace(distanceMiles, time);

        return new Run(date, distanceKilometres, distanceMiles, time, rating, milePace, kmPace);
    }

    public static Run withKilometers(String date, String distanceKilometres, String time, String rating){
        long d = RunUtils.dateToUnix(date);
        float distance = RunUtils.distanceToFloat(distanceKilometres);
        long t = RunUtils.timeToUnix(time);
        int r = RunUtils.ratingToInt(rating);

        return withKilometers(d, distance, t, r);
    }

    public static Run withMiles(long date, float distanceMiles, long time, int rating){
        float distanceKilometres = RunUtils.milesToKilometers(distanceMiles);
        long kmPace = RunUtils.calculatePace(distanceKilometres, time);
        long milePace = RunUtils.calculatePace(distanceMiles, time);

        return new Run(date, distanceKilometres, distanceMiles, time, rating, milePace, kmPace);
    }

    public static Run withMiles(String date, String distanceMiles, String time, String rating){
        long d = RunUtils.dateToUnix(date);
        float distance = RunUtils.distanceToFloat(distanceMiles);
        long t = RunUtils.timeToUnix(time);
        int r = RunUtils.ratingToInt(rating);

        return withMiles(d, distance, t, r);
    }

    @Deprecated
    /**
     * This no-arg constructor is required and should only be used by firebase, never by a user,
     * because it will lead to the object being in an invalid state where no fields are initialized.
     */
    public Run(){

    }

    public Run(Parcel in) {
        this.id = in.readString();
        this.date = in.readLong();
        this.distanceKilometres = in.readFloat();
        this.time = in.readLong();
        this.rating = in.readInt();
        this.distanceMiles = in.readFloat();
        this.milePace = in.readLong();
        this.kilometrePace = in.readLong();
    }

    @Exclude
    public float getDistance(String unit){
        return unit.equals("km") ? distanceKilometres : distanceMiles;
    }

    public float getDistanceKilometres(){
        return distanceKilometres;
    }

    public float getDistanceMiles(){
        return distanceMiles;
    }

    public long getTime(){
        return time;
    }

    public long getDate(){
        return date;
    }

    public int getRating(){
        return rating;
    }

    public String getId(){ return id; }

    public long getMilePace(){
        return milePace;
    }

    public long getKilometrePace() {
        return kilometrePace;
    }

    @Exclude
    /**
     * Automatically handles if distance is in miles or km and calls appropriate methods.
     * @param distance
     */
    public void setDistance(String distance){
        if (!(distance.contains("mi") || distance.contains("km"))){
            throw new IllegalArgumentException("Distance does not contain unit");
        }

        if (distance.contains("km")){
            setDistanceKilometres(distance);
        } else {
            setDistanceMiles(distance);
        }

        updatePace();

    }

    public void setDistanceKilometres(float distanceKilometres){
        this.distanceKilometres = distanceKilometres;
        updatePace();
    }

    @Exclude
    public void setDistanceKilometres(String distanceText){
        if (!distanceText.contains("km")){
            throw new IllegalArgumentException("Argument is not in kilometres");
        }

        distanceKilometres = RunUtils.distanceToFloat(distanceText);
        distanceMiles = RunUtils.kilometresToMiles(distanceKilometres);

        updatePace();
    }

    public void setDistanceMiles(float distanceMiles){
        this.distanceMiles = distanceMiles;
        updatePace();
    }

    @Exclude
    public void setDistanceMiles(String distanceText){
        if (!distanceText.contains("mi")){
            throw new IllegalArgumentException("Argument is not in miles");
        }

        distanceMiles = RunUtils.distanceToFloat(distanceText);
        distanceKilometres = RunUtils.milesToKilometers(distanceMiles);

        updatePace();
    }

    public void setTime(long time){
        this.time = time;
    }

    @Exclude
    public void setTime(String timeText){
        time = RunUtils.timeToUnix(timeText);
    }

    public void setDate(long date){
        this.date = date;
    }

    @Exclude
    public void setDate(String dateText){
        date = RunUtils.dateToUnix(dateText);
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    @Exclude
    public void setRating(String ratingText){
        rating = RunUtils.ratingToInt(ratingText);
    }

    public void setId(String pushKey){
        id = pushKey;
    }

    @Deprecated
    /**
     * Setter required for Firebase, should not be used by user as pace should never be set manually,
     * only computed off distance and time values, to avoid inconsistencies.
     */
    public void setKilometrePace(long kmPace){
        this.kilometrePace = kmPace;
    }

    @Deprecated
    /**
     * Setter required for Firebase, should not be used by user as pace should never be set manually,
     * only computed off distance and time values, to avoid inconsistencies.
     */
    public void setMilePace(long milePace){
        this.milePace = milePace;
    }

    /**
     * Updates pace values. Called when distance fields are modified and pace requires updating.
     */
    private void updatePace(){
        milePace = RunUtils.calculatePace(distanceMiles, time);
        kilometrePace = RunUtils.calculatePace(distanceKilometres, time);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Run)){
            return false;
        }

        Run run = (Run) obj;

        if (!this.getId().equals(run.getId())) return false;
        if (this.getDate() != run.getDate()) return false;
        if (this.getDistanceKilometres() != run.getDistanceKilometres()) return false;
        if (this.getRating() != run.getRating()) return false;
        if (this.getTime() != run.getTime()) return false;
        //No need to check for mile or km pace because pace is computed off time and distance.

        return true;
    }

    @Override
    public int hashCode() {
        int result = 15;
        result = 31 * result + Float.floatToIntBits(distanceKilometres);
        result = 31 * result + Float.floatToIntBits(distanceMiles);
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + rating;

        if (id != null){
            result = 31 * id.hashCode();
        }

        return result;
    }

    @Override
    public int compareTo(@NonNull Run o) {
        if (date == o.date){
            return 0;
        }

        if (date > o.date){
            return 1;
        }

        return -1;
    }

    @Override
    public String toString() {
        return distanceMiles + "mi - " + distanceKilometres + "km - date:" + date + " - time:"
                + time + " - rating:" + rating + " - id:" + id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeLong(date);
        parcel.writeFloat(distanceKilometres);
        parcel.writeLong(time);
        parcel.writeInt(rating);
        parcel.writeFloat(distanceMiles);
        parcel.writeLong(milePace);
        parcel.writeLong(kilometrePace);
        
    }

    @Exclude
    public static final Parcelable.Creator<Run> CREATOR = new Parcelable.Creator<Run>(){

        @Override
        public Run createFromParcel(Parcel parcel) {
            return new Run(parcel);
        }

        @Override
        public Run[] newArray(int i) {
            return new Run[i];
        }

    };

    public static class Predicates {

        public static Predicate isRunBetween(final long start, final long end) {
            return new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    Run tr = (Run) object;
                    return tr.getDate() >= start && tr.getDate() <= end;
                }
            };
        }

        public static Predicate isRunFromDistanceKm(final float distanceKm) {
            return new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    Run tr = (Run) object;
                    return Math.abs(tr.getDistanceKilometres() - distanceKm) < 0.01;
                }
            };
        }

        public static Predicate isRunFromDistanceMiles(final float distanceMiles) {
            return new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    Run tr = (Run) object;
                    return Math.abs(tr.getDistanceMiles() - distanceMiles) < 0.01;
                }
            };
        }
    }
}
