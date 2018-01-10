package com.example.tobias.run.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tobias.run.utils.Pair;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 12/18/2017.
 */

public class Distance implements Parcelable {

    public static final Parcelable.Creator<Distance> CREATOR = new Parcelable.Creator<Distance>() {

        @Override
        public Distance createFromParcel(Parcel source) {
            return new Distance(source);
        }

        @Override
        public Distance[] newArray(int size) {
            return new Distance[size];
        }
    };
    private double distanceKm;
    private double distanceMi;
    private double epsilon = 0.099;
    /*Contains the most common distance equivalences known by runners,
    * such as 5k = 3.1mi. This is done because of the inaccuracy when comparing double distance values due to
    * round-off errors and loss of precision when converting km values to mi and vice versa. Storing these
    * Common comparisons ensures that at least the most used ones will be correct.*/
    private List<Pair<Double, Double>> distanceEquivalences = new ArrayList<>();

    public Distance(double distance, Unit unit){

        if (unit == Unit.MILE){
            distanceMi = distance;
            distanceKm = mileToKm(distance);
        } else  {
            distanceKm = distance;
            distanceMi = kmToMile(distance);
        }
    }

    @Deprecated
    /**
     * This no-arg constructor is required and should only be used by firebase, never by a user,
     * because it will lead to the object being in an invalid state where no fields are initialized.
     */
    public Distance(){}

    private Distance(Parcel in){
        distanceKm = in.readDouble();
        distanceMi = in.readDouble();
    }

    private double kmToMile(double km){
        return km * 0.621371f;
    }

    private double mileToKm(double miles){
        return miles * 1.60934f;
    }

    /**
     * Requires distance in both units because one unit (the original one) will always be precise, while the
     * other unit will be computed off the first and thus not precise.
     * @param distanceKm
     * @param distanceMi
     * @return
     */
    public boolean equalsDistance(double distanceKm, double distanceMi){
        return Math.abs(this.distanceKm - distanceKm) <= epsilon || Math.abs(this.distanceMi - distanceMi) <= epsilon;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    @Deprecated
    /**
     * Method should only be used by firebase, as it might leave the object in an invalid state.
     * use setDistance(Distance.Unit) instead.
     */
    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getDistanceMi() {
        return distanceMi;
    }

    @Deprecated
    /**
     * Method should only be used by firebase, as it might leave the object in an invalid state.
     * use setDistance(Distance.Unit) instead.
     */
    public void setDistanceMi(double distanceMi) {
        this.distanceMi = distanceMi;
    }

    public double getDistance(Unit unit){
        if (unit == Unit.MILE){
            return getDistanceMi();
        }

        return getDistanceKm();
    }

    @Exclude
    public void setDistance(Unit unit, double distance){
        if (unit == Unit.MILE){
            distanceMi = distance;
            distanceKm = mileToKm(distance);
        } else  {
            distanceKm = distance;
            distanceMi = kmToMile(distance);
        }
    }

    @Override
    public int hashCode() {
        int result = 15;
        long hashKm = Double.doubleToLongBits(distanceKm);
        result = 31 * result + (int) (hashKm ^(hashKm >>> 32));
        long hashMi = Double.doubleToLongBits(distanceMi);
        result = 31 * result + (int) (hashMi ^(hashMi >>> 32));

        return result;
    }

    @Override
    public String toString() {
        return "Km:" + distanceKm + " - Mi:" + distanceMi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(distanceKm);
        dest.writeDouble(distanceMi);
    }

    public enum Unit {
        KM("km"), MILE("mi");

        private String value;

        Unit(String value){
            this.value = value;
        }


        @Override
        public String toString() {
            return value;
        }
    }
}