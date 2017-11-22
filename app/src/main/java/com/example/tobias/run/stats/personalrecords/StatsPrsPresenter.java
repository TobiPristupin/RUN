package com.example.tobias.run.stats.personalrecords;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.PersonalRecord;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.RunUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 11/8/2017.
 */

public class StatsPrsPresenter implements Observer<List<Run>> {

    private StatsPrsView view;
    private ObservableDatabase<Run> database;

    public StatsPrsPresenter(StatsPrsView view, ObservableDatabase<Run> database) {
        this.view = view;
        this.database = database;
        this.database.attachObserver(this);
        this.database.startQuery();
    }


    public void onDetachView(){
        database.detachObserver(this);
    }



    @Override
    public void updateData(List<Run> data) {
        List<PersonalRecord> personalRecords = new ArrayList<>();

        personalRecords.add(generateRecordFromDistance("Fastest 400m", .4f, data));
        personalRecords.add(generateRecordFromDistance("Fastest 1000m", 1f, data));
        personalRecords.add(generateRecordFromDistance("Fastest 5K", 5f, data));
        personalRecords.add(generateRecordFromDistance("Fastest 10K", 10f, data));
        personalRecords.add(generateRecordFromDistance("Fastest Half", 21f, data));
        personalRecords.add(generateRecordFromDistance("Fastest Marathon", 42f, data));

        view.setData(personalRecords);
    }

    private PersonalRecord generateRecordFromDistance(String title, float distance, List<Run> data){
        Run fastestRun = RunUtils.getFastestRun(distance, data);
        if (fastestRun == null){
            return PersonalRecord.createNotAchievedRecord(title);
        } else {
            return PersonalRecord.createAchievedRecord(title, fastestRun);
        }
    }

}
