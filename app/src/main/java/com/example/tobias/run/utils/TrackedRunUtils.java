package com.example.tobias.run.utils;

import com.example.tobias.run.data.TrackedRun;

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
    public static List<TrackedRun> filterRun(List<TrackedRun> trackedRuns, Predicate predicate){
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
    public static double getMileageBetween(long start, long end){
        return 1;
    }
}
