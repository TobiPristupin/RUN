package com.example.tobias.run.database;

import com.example.tobias.run.utils.ConversionManager;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tobi on 9/4/2017.
 */

public class TrackedRunRepository {

    private static ArrayList<TrackedRun> allTrackedRuns;
    private static ArrayList<TrackedRun> trackedRunsToDisplay;
    static {
        allTrackedRuns = new ArrayList<>();
        trackedRunsToDisplay = new ArrayList<>();
    }


    public static void addRunToList(TrackedRun trackedRun){
        allTrackedRuns.add(trackedRun);
    }

    public static void addAllRunsToList(ArrayList<TrackedRun> trackedRuns){
        allTrackedRuns.addAll(trackedRuns);
    }

    public static ArrayList<TrackedRun> getRuns(){
        return allTrackedRuns;
    }

    public static ArrayList<TrackedRun> getMonthRuns(){
        trackedRunsToDisplay.clear();
        for (TrackedRun tr : allTrackedRuns){
            if (tr.getDate() >= ConversionManager.getStartOfWeek() && tr.getDate() <= ConversionManager.getEndOfWeek()){
                trackedRunsToDisplay.add(tr);
            }
        }
        return trackedRunsToDisplay;
    }

    public static ArrayList<TrackedRun> getWeekRuns(){
        trackedRunsToDisplay.clear();
        for (TrackedRun tr : allTrackedRuns){
            if (tr.getDate() >= ConversionManager.getStartOfWeek() && tr.getDate() <= ConversionManager.getEndOfWeek()){
                trackedRunsToDisplay.add(tr);
            }
        }
        return trackedRunsToDisplay;
    }

    public static ArrayList<TrackedRun> getYearRuns(){
        trackedRunsToDisplay.clear();
        for (TrackedRun tr : allTrackedRuns){
            if (tr.getDate() >= ConversionManager.getStartOfYear() && tr.getDate() <= ConversionManager.getEndOfYear()){
                trackedRunsToDisplay.add(tr);
            }
        }
        return trackedRunsToDisplay;
    }
}
