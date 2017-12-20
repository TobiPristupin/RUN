package com.example.tobias.run.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.tobias.run.utils.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 12/18/2017.
 */

public class Distance implements Comparable<Distance>, Parcelable {

    private float distanceKm;
    private float distanceMi;

    /*Contains the most common distance equivalences known by runners,
    * such as 5k = 3.1mi. This is done because of the inaccuracy when comparing float distance values due to
    * round-off errors and loss of precision when converting km values to mi and vice versa. Storing these
    * Common comparisons ensures that at least the most used ones will be correct.*/
    private List<Pair<Float, Float>> distanceEquivalences = new ArrayList<>();

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

    public Distance(float distance, Unit unit){
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
        distanceEquivalences.add(new Pair<>(1.6f, 1f));
        distanceEquivalences.add(new Pair<>(5f, 3.1f));
        distanceEquivalences.add(new Pair<>(10f, 6.2f));
        distanceEquivalences.add(new Pair<>(21f, 13.1f));
        distanceEquivalences.add(new Pair<>(42f, 26.2f));
    }

    private float roundNearestTenth(float a){
         return (float) Math.round(a * 10) / 10;
    }

    private float kmToMile(float km){
        for (Pair<Float, Float> pair : distanceEquivalences){
            if (pair.first == km){
                return pair.second;
            }
        }

           return roundNearestTenth(km * 0.621371f);
    }

    private float mileToKm(float miles){
        for (Pair<Float, Float> pair : distanceEquivalences){
            if (pair.second == miles){
                return pair.first;
            }
        }

        return roundNearestTenth(miles * 1.60934f);
    }

    @Override
    public int compareTo(@NonNull Distance o) {
        return Math.round(distanceKm - o.distanceKm);
    }

    public boolean equalsDistance(float distance, Unit unit){
        if (unit == Unit.MILE){
            return Math.abs(distanceMi - distance) < 0.099;
        }

        return Math.abs(distanceKm - distance) < 0.099;
    }


    public float getDistanceKm() {
        return distanceKm;
    }

    public float getDistanceMi() {
        return distanceMi;
    }

    public float getDistance(Unit unit){
        if (unit == Unit.MILE){
            return getDistanceMi();
        }

        return getDistanceKm();
    }

    public void setDistanceKm(float distanceKm) {
        distanceKm = roundNearestTenth(distanceKm);
        this.distanceKm = distanceKm;
        this.distanceMi = kmToMile(distanceKm);
    }

    public void setDistanceMi(float distanceMi) {
        distanceMi = roundNearestTenth(distanceMi);
        this.distanceMi = distanceMi;
        this.distanceKm = mileToKm(distanceMi);
    }

    @Override
    public int hashCode() {
        int result = 15;
        result = 31 * result + Float.floatToIntBits(distanceKm);
        result = 31 * result + Float.floatToIntBits(distanceMi);

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
        dest.writeFloat(distanceKm);
        dest.writeFloat(distanceMi);
    }

    private Distance(Parcel in){
        distanceKm = in.readFloat();
        distanceMi = in.readFloat();
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
