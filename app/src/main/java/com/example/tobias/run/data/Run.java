package com.example.tobias.run.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.tobias.run.utils.RunUtils;
import com.google.firebase.database.Exclude;

import org.joda.time.Period;


/**
 * Object to represent a run.
 */
public class Run implements Parcelable, Comparable<Run> {

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
    private Distance distance;
    private long time;
    private long date;
    private int rating;
    private long milePace;
    private long kilometrePace;
    private String id = null;

    public Run(Distance distance, long time, long date, int rating) {
        this.distance = distance;
        this.time = time;
        this.date = date;
        this.rating = rating;
        this.kilometrePace = calculatePace(this.distance.getDistanceKm(), time);
        this.milePace = calculatePace(this.distance.getDistanceMi(), time);
    }

    @Deprecated
    /**
     * This no-arg constructor is required and should only be used by firebase, never by a user,
     * because it will lead to the object being in an invalid state where no fields are initialized.
     */
    public Run(){}

    private Run(Parcel in) {
        this.distance = in.readParcelable(Distance.class.getClassLoader());
        this.id = in.readString();
        this.date = in.readLong();
        this.time = in.readLong();
        this.rating = in.readInt();
        this.milePace = in.readLong();
        this.kilometrePace = in.readLong();
    }

    public static Run withKilometers(double distanceKm, long time, long date, int rating){
        Distance distance = new Distance(distanceKm, Distance.Unit.KM);
        return new Run(distance, time, date, rating);
    }

    public static Run withKilometers(String distanceKm, String time, String date, String rating){
        long d = RunUtils.dateToUnix(date);
        double distance = RunUtils.distanceToDouble(distanceKm);
        long t = RunUtils.timeToUnix(time);
        int r = RunUtils.ratingToInt(rating);
        return withKilometers(distance, t, d, r);
    }

    public static Run withMiles(double distanceMi, long time, long date, int rating){
        Distance distance = new Distance(distanceMi, Distance.Unit.MILE);
        return new Run(distance, time, date, rating);
    }

    public static Run withMiles(String distanceMi, String time, String date, String rating){
        long d = RunUtils.dateToUnix(date);
        double distance = RunUtils.distanceToDouble(distanceMi);
        long t = RunUtils.timeToUnix(time);
        int r = RunUtils.ratingToInt(rating);

        return withMiles(distance, t, d, r);
    }

    /**
     * Updates pace values. Called when distance fields are modified and pace requires updating.
     */
    private void updatePace(){
        if (distance != null){
            milePace = calculatePace(distance.getDistanceMi(), time);
            kilometrePace = calculatePace(distance.getDistanceKm(), time);
        }
    }

    private long calculatePace(double distance, long time){
        //Period is inputted time in millis and converts it to hh:mm:ss
        Period period = new Period(time);
        double timeInSeconds = period.getHours() * 3600f + period.getMinutes() * 60f + period.getSeconds();
        double pace = timeInSeconds / distance;
        //Multiply pace by 1000 to convert it to millis from seconds.
        return (long) pace * 1000;
    }



    @Exclude public double getDistance(Distance.Unit unit){
        return distance.getDistance(unit);
    }

    public Distance getDistance(){
        return distance;
    }

    @Exclude public void setDistance(String distanceString){
        String km = Distance.Unit.KM.toString();
        String mi = Distance.Unit.MILE.toString();

        if (!(distanceString.contains(km) || distanceString.contains(mi))){
            throw new IllegalArgumentException("Distance does not contain unit");
        }

        double d = RunUtils.distanceToDouble(distanceString);

        if (distanceString.contains(km)){
            setDistanceKilometres(d);
        } else {
            setDistanceMiles(d);
        }

        updatePace();
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time = time;
        updatePace();
    }

    public long getDate(){
        return date;
    }

    public void setDate(long date){
        this.date = date;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public String getId(){ return id; }

    public void setId(String pushKey){
        id = pushKey;
    }

    @Exclude public long getPace(Distance.Unit unit){
        if (unit == Distance.Unit.KM){
            return kilometrePace;
        }

        return milePace;
    }

    @Exclude public void setTime(String timeText){
        time = RunUtils.timeToUnix(timeText);
        updatePace();
    }

    @Exclude public void setDate(String dateText){
        date = RunUtils.dateToUnix(dateText);
    }

    @Exclude public void setRating(String ratingText){
        rating = RunUtils.ratingToInt(ratingText);
    }

    public void setDistance(Distance distance){
        this.distance = distance;
        updatePace();
    }

    @Exclude public void setDistanceKilometres(double distanceKilometres){
        distance.setDistance(Distance.Unit.KM, distanceKilometres);
        updatePace();
    }

    @Exclude  public void setDistanceMiles(double distanceMiles){
        distance.setDistance(Distance.Unit.MILE, distanceMiles);
        updatePace();
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




    @Override
    public int hashCode() {
        int result = 15;
        result = 31 * result + distance.hashCode();
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + rating;

        if (id != null){
            result = 31 * id.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Run)){
            return false;
        }

        Run run = (Run) obj;

        if (this.getId() != null && run.getId() != null && !this.getId().equals(run.getId())) return false;
        if (this.getDate() != run.getDate()) return false;
        if (this.getDistance(Distance.Unit.KM) != run.getDistance(Distance.Unit.KM)) return false;
        if (this.getRating() != run.getRating()) return false;
        if (this.getTime() != run.getTime()) return false;
        //No need to check for mile or km pace because pace is computed off time and distance.

        return true;
    }

    @Override
    public String toString() {
        return distance.toString() + " - date:" + date + " - time:"
                + time + " - rating:" + rating + " - id:" + id;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(distance, i);
        parcel.writeString(id);
        parcel.writeLong(date);
        parcel.writeLong(time);
        parcel.writeInt(rating);
        parcel.writeLong(milePace);
        parcel.writeLong(kilometrePace);
    }

}
