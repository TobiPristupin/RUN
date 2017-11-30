package com.example.tobias.run.stats.personalrecords;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;

/**
 * Created by Tobi on 11/28/2017.
 */

public class StatsPrsPresenter {

    private StatsPrsView view;
    private ObservableDatabase<Run> database;

    public StatsPrsPresenter(StatsPrsView view, ObservableDatabase<Run> database) {
        this.view = view;
        this.database = database;
    }


}
