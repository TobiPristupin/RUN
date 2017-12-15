package com.example.tobias.run.stats.personalrecords;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.RunUtils;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatsPrsPresenter implements Observer<List<Run>> {

    private StatsPrsView view;
    private ObservableDatabase<Run> model;
    private List<Run> runList;

    public StatsPrsPresenter(StatsPrsView view, ObservableDatabase<Run> model) {
        this.view = view;
        this.model = model;

        this.model.attachObserver(this);
    }

    @Override
    public void updateData(List<Run> data) {
        runList = data;

        updateChartData();
    }

    public void onDetachView(){
        model.detachObserver(this);
    }

    private void updateChartData(){
        update400mChartData();
        updateMileChartData();
        update5kChartData();
        update10kChartData();
        update21kChartData();
        update42kChartData();
    }

    private void update400mChartData(){
        List<Run> runs400m = RunUtils.filterList(runList, Run.Predicates.isRunFromDistanceKm(.4f));

        List<Entry> scatterData = null;
        List<Entry> bestRunData = null;
        List<Entry> averageRunData = null;

        if (runs400m.size() > 0) {
            scatterData = new ArrayList<>();

            Run fastestRun = RunUtils.getFastestRun(runs400m, false);
            //Fastest run will always be added
            scatterData.add(new Entry(1, fastestRun.getTime()));

            //Start at 2 because index 0 goes outside the graph and index 1 is used by fastestRun
            int counter = 2;
            for (int i = 1; i < runs400m.size(); i++){
                Run r = runs400m.get(i);

                /*Run has to be less than 75% slower than best run to be added to graph. Runs not
                added to graph will still count in average data.*/
                if (fastestRun.getTime() * 1.5 > r.getTime()){
                    scatterData.add(new Entry(counter, r.getTime()));
                    counter++;
                }
            }

            bestRunData = new ArrayList<>();
            bestRunData.add(new Entry(0, fastestRun.getTime()));
            bestRunData.add(new Entry(scatterData.size() + 1, fastestRun.getTime()));

            averageRunData = new ArrayList<>();
            float averageTime = RunUtils.getAverageTime(runs400m);
            averageRunData.add(new Entry(0, averageTime));
            averageRunData.add(new Entry(scatterData.size() + 1, averageTime));
        }

        view.set400mChartData(scatterData, bestRunData, averageRunData);
    }

    private void updateMileChartData(){
        List<Entry> data = Arrays.asList(new Entry(0, 1));
        view.setMileChartData(data, data, data);
    }

    private void update5kChartData(){
        List<Entry> data = Arrays.asList(new Entry(0, 1));
        view.set5kChartData(data, data, data);
    }

    private void update10kChartData(){
        List<Entry> data = Arrays.asList(new Entry(0, 1));
        view.set10kChartData(data, data, data);
    }

    private void update21kChartData(){
        List<Entry> data = Arrays.asList(new Entry(0, 1));
        view.set21kChartData(data, data, data);
    }

    private void update42kChartData(){
        List<Entry> data = Arrays.asList(new Entry(0, 1));
        view.set42kChartData(data, data, data);
    }

}
