package com.example.tobias.run.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tobias.run.utils.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 12/18/2017.
 */

public class Distance implements Parcelable {

    private double distanceKm;
    private double distanceMi;

    /*Contains the most common distance equivalences known by runners,
    * such as 5k = 3.1mi. This is done because of the inaccuracy when comparing double distance values due to
    * round-off errors and loss of precision when converting km values to mi and vice versa. Storing these
    * Common comparisons ensures that at least the most used ones will be correct.*/
    private List<Pair<Double, Double>> distanceEquivalences = new ArrayList<>();

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

    public Distance(double distance, Unit unit){
        initDistanceEquivalences();
        roundNearestTenth(distance);

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

    private void initDistanceEquivalences(){
        distanceEquivalences.add(new Pair<>(.4, .25));
        distanceEquivalences.add(new Pair<>(1.6, 1d));
        distanceEquivalences.add(new Pair<>(5d, 3.1));
        distanceEquivalences.add(new Pair<>(10d, 6.2));
        distanceEquivalences.add(new Pair<>(21d, 13.1));
        distanceEquivalences.add(new Pair<>(42d, 26.2));
    }

    private double roundNearestTenth(double a){
         return (double) Math.round(a * 10) / 10;
    }

    private double kmToMile(double km){
        for (Pair<Double, Double> pair : distanceEquivalences){
            if (pair.first == km){
                return pair.second;
            }
        }

           return roundNearestTenth(km * 0.621371f);
    }

    private double mileToKm(double miles){
        for (Pair<Double, Double> pair : distanceEquivalences){
            if (pair.second == miles){
                return pair.first;
            }
        }

        return roundNearestTenth(miles * 1.60934f);
    }


    public boolean equalsDistance(double distance, Unit unit){
        if (unit == Unit.MILE){
            return Math.abs(distanceMi - distance) < 0.099;
        }

        return Math.abs(distanceKm - distance) < 0.099;
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

    public void setDistanceKm(double distanceKm) {
        distanceKm = roundNearestTenth(distanceKm);
        this.distanceKm = distanceKm;
        this.distanceMi = kmToMile(distanceKm);
    }

    public void setDistanceMi(double distanceMi) {
        distanceMi = roundNearestTenth(distanceMi);
        this.distanceMi = distanceMi;
        this.distanceKm = mileToKm(distanceMi);
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

    private Distance(Parcel in){
        distanceKm = in.readDouble();
        distanceMi = in.readDouble();
    }

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
}
