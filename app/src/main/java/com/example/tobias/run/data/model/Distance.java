package com.example.tobias.run.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Model class to represent Distance in kilometres and miles. Used in Run class.
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
    private double floatingPointEpsilon = 0.099;

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
        return Math.abs(this.distanceKm - distanceKm) <= floatingPointEpsilon || Math.abs(this.distanceMi - distanceMi) <= floatingPointEpsilon;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getDistanceMi() {
        return distanceMi;
    }

    public double getDistance(Unit unit){
        if (unit == Unit.MILE){
            return getDistanceMi();
        }

        return getDistanceKm();
    }

    @Deprecated
    /**
     * Method should only be used by firebase, as it might leave the object in an invalid state.
     * use setDistance(Distance.Unit) instead.
     */
    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    @Deprecated
    /**
     * Method should only be used by firebase, as it might leave the object in an invalid state.
     * use setDistance(Distance.Unit) instead.
     */
    public void setDistanceMi(double distanceMi) {
        this.distanceMi = distanceMi;
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


        public static Unit get(String value) {
            switch (value.toLowerCase()) {
                case "km":
                    return KM;
                case "mi":
                    return MILE;
                default:
                    throw new RuntimeException();
            }
        }

        @Override
        public String toString() {
            return value;
        }
    }
}