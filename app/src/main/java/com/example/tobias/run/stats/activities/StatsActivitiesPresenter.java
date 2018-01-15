package com.example.tobias.run.stats.activities;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.interfaces.Observer;

import java.util.List;

/**
 * Created by Tobi on 1/12/2018.
 */

public class StatsActivitiesPresenter implements Observer<List<Run>> {

    private StatsActivitiesView view;
    private ObservableDatabase<Run> model;

    public StatsActivitiesPresenter(StatsActivitiesView view, ObservableDatabase<Run> model) {
        this.view = view;
        this.model = model;

        this.model.attachObserver(this);
    }

    public void onDetachView(){
        this.model.detachObserver(this);
    }

    @Override
    public void updateData(List<Run> data) {

    }
}
