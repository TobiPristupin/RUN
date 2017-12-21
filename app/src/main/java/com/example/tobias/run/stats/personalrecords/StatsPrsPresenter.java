package com.example.tobias.run.stats.personalrecords;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.Distance;
import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.RunUtils;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
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

    public void onDetachView(){
        model.detachObserver(this);
    }

    @Override
    public void updateData(List<Run> data) {
        runList = data;

        updateChartData();
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
        List<Run> runs400m = RunUtils.filterList(runList, Run.Predicates.isRunFromDistance(.4f, Distance.Unit.KM));

        List<Entry> scatterData = calculateScatterData(runs400m);
        int scatterDataSize = scatterData == null ? 0  : scatterData.size();
        List<Entry> bestRunData = calculateBestRunData(runs400m, scatterDataSize);
        List<Entry> averageRunData = calculateAverageRunData(runs400m, scatterDataSize);

        view.set400mChartData(scatterData, bestRunData, averageRunData);
    }

    private void updateMileChartData(){
        List<Run> runsMile = RunUtils.filterList(runList, Run.Predicates.isRunFromDistance(1f, Distance.Unit.MILE));

        List<Entry> scatterData = calculateScatterData(runsMile);
        int scatterDataSize = scatterData == null ? 0  : scatterData.size();
        List<Entry> bestRunData = calculateBestRunData(runsMile, scatterDataSize);
        List<Entry> averageRunData = calculateAverageRunData(runsMile, scatterDataSize);

        view.setMileChartData(scatterData, bestRunData, averageRunData);
    }

    private void update5kChartData(){
        List<Run> runs5k = RunUtils.filterList(runList, Run.Predicates.isRunFromDistance(5f, Distance.Unit.KM));

        List<Entry> scatterData = calculateScatterData(runs5k);
        int scatterDataSize = scatterData == null ? 0  : scatterData.size();
        List<Entry> bestRunData = calculateBestRunData(runs5k, scatterDataSize);
        List<Entry> averageRunData = calculateAverageRunData(runs5k, scatterDataSize);

        view.set5kChartData(scatterData, bestRunData, averageRunData);
    }

    private void update10kChartData(){
        List<Run> runs10k = RunUtils.filterList(runList, Run.Predicates.isRunFromDistance(10f, Distance.Unit.KM));

        List<Entry> scatterData = calculateScatterData(runs10k);
        int scatterDataSize = scatterData == null ? 0  : scatterData.size();
        List<Entry> bestRunData = calculateBestRunData(runs10k, scatterDataSize);
        List<Entry> averageRunData = calculateAverageRunData(runs10k, scatterDataSize);

        view.set10kChartData(scatterData, bestRunData, averageRunData);
    }

    private void update21kChartData(){
        List<Run> runs21k = RunUtils.filterList(runList, Run.Predicates.isRunFromDistance(21f, Distance.Unit.KM));

        List<Entry> scatterData = calculateScatterData(runs21k);
        int scatterDataSize = scatterData == null ? 0  : scatterData.size();
        List<Entry> bestRunData = calculateBestRunData(runs21k, scatterDataSize);
        List<Entry> averageRunData = calculateAverageRunData(runs21k, scatterDataSize);

        view.set21kChartData(scatterData, bestRunData, averageRunData);
    }

    private void update42kChartData() {
        List<Run> runs42k = RunUtils.filterList(runList, Run.Predicates.isRunFromDistance(42, Distance.Unit.KM));

        List<Entry> scatterData = calculateScatterData(runs42k);
        int scatterDataSize = scatterData == null ? 0  : scatterData.size();
        List<Entry> bestRunData = calculateBestRunData(runs42k, scatterDataSize);
        List<Entry> averageRunData = calculateAverageRunData(runs42k, scatterDataSize);

        view.set42kChartData(scatterData, bestRunData, averageRunData);
    }

    private @Nullable List<Entry> calculateScatterData(List<Run> runs){
        if (runs.size() < 1){
            return null;
        }

        List<Entry> scatterData = new ArrayList<>();
        Run fastestRun = RunUtils.getFastestRun(runs);

        //Fastest run will always be added
        scatterData.add(new Entry(1, fastestRun.getTime()));

        //Start at 2 because index 0 is outside of the graph and index 1 is already occupied by fastestRun
        int counter = 2;
        for (int i = 1; i < runs.size(); i++){
            Run r = runs.get(i);

            /*Run has to be less than 50% slower than best run to be added to graph. Runs not
            added to graph will still count in average data.*/
            if (fastestRun.getTime() * 1.5 > r.getTime()){
                scatterData.add(new Entry(counter, r.getTime()));
                counter++;
            }
        }

        return scatterData;
    }

    /**
     *
     * @param runs
     * @param scatterDataSize bestRunData is represented as a line in the graph , and the x-value of
     *                       the endpoint of the line has to be scatterDataSize + 1, to add the necessary
     *                       padding to the graph and avoid the scatter data dots going outside of the graph.
     * @return
     */
    private @Nullable List<Entry> calculateBestRunData(List<Run> runs, int scatterDataSize){
        if (runs.size() < 1){
            return null;
        }

        Run fastestRun = RunUtils.getFastestRun(runs);
        List<Entry> bestRunData = new ArrayList<>();

        bestRunData.add(new Entry(0, fastestRun.getTime()));
        bestRunData.add(new Entry(scatterDataSize + 1, fastestRun.getTime()));

        return bestRunData;
    }

    /**
     *
     * @param runs
     * @param scatterDataSize averageRunData is represented as a line in the graph , and the x-value of
     *                       the endpoint of the line has to be scatterDataSize + 1, to add the necessary
     *                       padding to the graph and avoid the scatter data dots going outside of the graph.
     * @return
     */
    private @Nullable List<Entry> calculateAverageRunData(List<Run> runs, int scatterDataSize){
        if (runs.size() < 1){
            return null;
        }

        List<Entry> averageRunData = new ArrayList<>();

        double averageTime = RunUtils.getAverageTime(runs);
        averageRunData.add(new Entry(0, (float) averageTime));
        averageRunData.add(new Entry(scatterDataSize + 1, (float) averageTime));

        return averageRunData;
    }

}
