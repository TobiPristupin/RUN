package com.example.tobias.run.data;

/**
 * Created by Tobi on 11/8/2017.
 */

public class PersonalRecord {

    private boolean achieved;
    private String title;
    private TrackedRun trackedRun;

    private PersonalRecord(boolean achieved, String title, TrackedRun trackedRun) {
        this.achieved = achieved;
        this.title = title;
        this.trackedRun = trackedRun;
    }

    public static PersonalRecord createAchievedRecord(String title, TrackedRun trackedRun){
        return new PersonalRecord(true, title, trackedRun);
    }

    public static PersonalRecord createNotAchievedRecord(String title){
        return new PersonalRecord(false, title, null);
    }

    public boolean isAchieved() {
        return achieved;
    }

    public String getTitle() {
        return title;
    }

    public TrackedRun getTrackedRun() {
        if (trackedRun == null){
            throw new IllegalStateException("This record isn't achieved, so it has no Tracked Run associated with it");
        }

        return trackedRun;
    }
}