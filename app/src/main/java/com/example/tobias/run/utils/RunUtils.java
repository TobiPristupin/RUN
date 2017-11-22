package com.example.tobias.run.utils;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.RunPredicates;
import com.example.tobias.run.data.SharedPreferenceRepository;

import org.apache.commons.collections.Predicate;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 10/15/2017.
 */

public class RunUtils {

    private RunUtils(){
        //Private constructor to ensure noninstantiability
    }

    /**
     * Filters run given a predicate and a list of runs. Does not modify list of runs.
     * @param runs
     * @param predicate
     * @return filtered ArrayList
     */
    public static List<Run> filterList(List<Run> runs, Predicate predicate){
        List<Run> filteredList = new ArrayList<>();
        for (Run tr : runs){
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
    public static float getMileageBetween(long start, long end, List<Run> data, SharedPreferenceRepository sharedPref){
        List<Run> filteredList = new ArrayList<>();
        filteredList.addAll(filterList(data, RunPredicates.isRunBetween(start, end)));

        float mileage = 0;

        for (Run tr : filteredList){
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

    public static String getDistanceText(Run currentItem, SharedPreferenceRepository sharedPref){
        String text;
        String unit = getDistanceUnit(sharedPref);

        if (unit.equals("km")){
            text = ConversionUtils.distanceToString(currentItem.getDistanceKilometres(), unit);
        } else {
            text = ConversionUtils.distanceToString(currentItem.getDistanceMiles(), unit);
        }

        return text;
    }

    public static String getPaceText(Run currentItem, SharedPreferenceRepository sharedPref){
        String text;
        String unit = getDistanceUnit(sharedPref);

        if (unit.equals("km")){
            text = ConversionUtils.paceToString(currentItem.getKmPace(), "km");
        } else {
            text = ConversionUtils.paceToString(currentItem.getMilePace(), "mi");
        }

        return text;
    }

    /**
     * Returns the fastest run (less time) given a distance.
     * @param distanceKilometers
     * @return Fastest run (less time)
     */
    public static @Nullable
    Run getFastestRun(float distanceKilometers, List<Run> list){
        Run fastest = null;

        for (Run run : list){

            //Runs that have time 0 will not be counted
            if (run.getTime() == 0){
                continue;
            }

            if (ConversionUtils.round(run.getDistanceKilometres(), 1) != distanceKilometers){
                continue;
            }

            if (fastest == null){
                fastest = run;
            } else if (run.getTime() < fastest.getTime()){
                fastest = run;
            }

        }

        return fastest;
    }

    /**
     *
     * @param distance distance in any format.
     * @param unixTime time of run in unix timestamp format
     * @return pace per unit of distance in unix timestamp format
     */
    public static long calculatePace(float distance, long unixTime){
        //Period is inputted time in millis and converts it to hh:mm:ss
        Period period = new Period(unixTime);
        float timeInSeconds = period.getHours() * 3600f + period.getMinutes() * 60f + period.getSeconds();
        float pace = timeInSeconds / distance;
        //Multiply pace by 1000 to convert it to millis from seconds.
        return (long) pace * 1000;
    }

    public static float kilometresToMiles(float km){
        return km * 0.621371f;
    }

    public static float milesToKilometers(float mi){
        return mi * 1.60934f;
    }
}
