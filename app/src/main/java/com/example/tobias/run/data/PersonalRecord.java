package com.example.tobias.run.data;

/**
 * Created by Tobi on 11/8/2017.
 */

public class PersonalRecord {

    private boolean achieved;
    private String title;
    private Run run;

    private PersonalRecord(boolean achieved, String title, Run run) {
        this.achieved = achieved;
        this.title = title;
        this.run = run;
    }

    public static PersonalRecord createAchievedRecord(String title, Run run){
        return new PersonalRecord(true, title, run);
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

    public Run getRun() {
        if (run == null){
            throw new IllegalStateException("This record isn't achieved, so it has no Tracked Run associated with it");
        }

        return run;
    }
}