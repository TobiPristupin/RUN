package com.example.tobias.run.utils;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.data.TrackedRunPredicates;

import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 10/15/2017.
 */

public class TrackedRunUtils {

    /**
     * Filters run given a predicate and a list of runs. Does not modify list of runs.
     * @param trackedRuns
     * @param predicate
     * @return filtered ArrayList
     */
    public static List<TrackedRun> filterList(List<TrackedRun> trackedRuns, Predicate predicate){
        List<TrackedRun> filteredList = new ArrayList<>();
        for (TrackedRun tr : trackedRuns){
            if (predicate.evaluate(tr)){
                filteredList.add(tr);
            }
        }

        return filteredList;
    }

    /**
     * Gets mileage between a period of time.
     * @param start time in unix timestamp
     * @param end
     * @return mileage in unit according to shared preferences.
     */
    public static float getMileageBetween(long start, long end, List<TrackedRun> data, SharedPreferenceRepository sharedPref){
        List<TrackedRun> filteredList = new ArrayList<>();
        filteredList.addAll(filterList(data, TrackedRunPredicates.isRunBetween(start, end)));

        float mileage = 0;

        for (TrackedRun tr : filteredList){
            if (getDistanceUnit(sharedPref).equals("km")){
                mileage += tr.getDistanceKilometres();
            } else {
                mileage += tr.getDistanceMiles();
            }
        }

        return mileage;
    }

    public static float addMileage(float[] mileage){
        float sum = 0;
        for (float f : mileage){
            sum += f;
        }

        return sum;
    }

    private static String getDistanceUnit(SharedPreferenceRepository sharedPref){
        return sharedPref.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
    }

    public static String getDistanceText(TrackedRun currentItem, SharedPreferenceRepository sharedPref){
        String text;
        String unit = getDistanceUnit(sharedPref);

        if (unit.equals("km")){
            text = ConversionManager.distanceToString(currentItem.getDistanceKilometres(), unit);
        } else {
            text = ConversionManager.distanceToString(currentItem.getDistanceMiles(), unit);
        }

        return text;
    }

    public static String getPaceText(TrackedRun currentItem, SharedPreferenceRepository sharedPref){
        String text;
        String unit = getDistanceUnit(sharedPref);

        if (unit.equals("km")){
            text = ConversionManager.paceToString(currentItem.getKmPace(), "km");
        } else {
            text = ConversionManager.paceToString(currentItem.getMilePace(), "mi");
        }

        return text;
    }

    /**
     * Returns the fastest run (less time) given a distance.
     * @param distanceKilometers
     * @return Fastest run (less time)
     */
    public static @Nullable TrackedRun getFastestRun(float distanceKilometers, List<TrackedRun> list){
        TrackedRun fastest = null;

        for (TrackedRun trackedRun : list){

            //Runs that have time 0 will not be counted
            if (trackedRun.getTime() == 0){
                continue;
            }

            if (trackedRun.getDistanceKilometres() != distanceKilometers){
                continue;
            }

            if (fastest == null){
                fastest = trackedRun;
            } else if (trackedRun.getTime() < fastest.getTime()){
                fastest = trackedRun;
            }

        }

        return fastest;
    }

}
