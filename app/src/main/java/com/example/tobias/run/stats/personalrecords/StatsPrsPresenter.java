package com.example.tobias.run.stats.personalrecords;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.PersonalRecord;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.TrackedRunUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 11/8/2017.
 */

public class StatsPrsPresenter implements Observer<List<TrackedRun>> {

    private StatsPrsView view;
    private ObservableDatabase<TrackedRun> database;

    public StatsPrsPresenter(StatsPrsView view, ObservableDatabase<TrackedRun> database) {
        this.view = view;
        this.database = database;
        this.database.attachObserver(this);
        this.database.startQuery();
    }


    public void onDetachView(){
        database.detachObserver(this);
    }



    @Override
    public void updateData(List<TrackedRun> data) {
        List<PersonalRecord> personalRecords = new ArrayList<>();

        TrackedRun fastest400m = TrackedRunUtils.getFastestRun(.4f, data);
        if (fastest400m == null){
            personalRecords.add(new PersonalRecord.Builder(false, "400 Meters", false).build());
        } else {
            personalRecords.add(new PersonalRecord.Builder(true, "400 Meters", false).build());
        }
    }

}
