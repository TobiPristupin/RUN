package com.example.tobias.run.stats;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.data.TrackedRunPredicates;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.TrackedRunUtils;
import com.github.mikephil.charting.data.BarEntry;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 10/30/2017.
 */

public class StatsMileagePresenter implements Observer<List<TrackedRun>> {

    private StatsMileageView view;
    private ObservableDatabase model;

    public StatsMileagePresenter(StatsMileageView view, ObservableDatabase<TrackedRun> model) {
        this.view = view;
        this.model = model;

        this.model.attachObserver(this);
        this.model.startQuery();
    }

    @Override
    public void updateData(List<TrackedRun> data) {
        updateBarChartMonth(data);
        updateBarChart3Month(data);
    }


    public void onDetachView(){
        model.detachObserver(this);
    }

    private void updateBarChartMonth(List<TrackedRun> data){
        List<TrackedRun> filteredList = new ArrayList<>();
        filteredList.addAll(TrackedRunUtils.filterRun(data, TrackedRunPredicates.isRunFromMonth()));

        float mileageSum = addMileage(filteredList);

        List<BarEntry> barEntries = new ArrayList<>();


        if (mileageSum != 0){
            barEntries.add(new BarEntry(1, mileageSum));
            view.setGraphMonthData(barEntries);
        }

        DateTime dateTime = new DateTime();
        String monthStr = dateTime.monthOfYear().getAsText();
        view.setGraphMonthXLabel(new String[]{monthStr});
    }

    private void updateBarChart3Month(List<TrackedRun> data){
        List<TrackedRun> filteredList = new ArrayList<>();
        filteredList.addAll(TrackedRunUtils.filterRun(data, TrackedRunPredicates.isRunFromPast2Months()));

        float mileageSum2MonthsPrevious = addMileage(TrackedRunUtils.filterRun(filteredList, TrackedRunPredicates.isRunFromMonthMinusMonth(2)));
        float mileageSumPreviousMonth = addMileage(TrackedRunUtils.filterRun(filteredList, TrackedRunPredicates.isRunFromMonthMinusMonth(1)));
        float mileageSumMonth = addMileage(TrackedRunUtils.filterRun(filteredList, TrackedRunPredicates.isRunFromMonth()));

        List<BarEntry> barEntries = new ArrayList<>();


        if (mileageSum2MonthsPrevious + mileageSumPreviousMonth + mileageSumMonth != 0){
            barEntries.add(new BarEntry(1, mileageSum2MonthsPrevious));
            barEntries.add(new BarEntry(2, mileageSumPreviousMonth));
            barEntries.add(new BarEntry(3, mileageSumMonth));
            view.setGraph3MonthData(barEntries);
        }

        DateTime dateTime = new DateTime();
        String[] xAxisLabelValues = {
                dateTime.minusMonths(2).monthOfYear().getAsText(),
                dateTime.minusMonths(1).monthOfYear().getAsText(),
                dateTime.monthOfYear().getAsText()
        };
        view.setGraph3MonthXLabel(xAxisLabelValues);
    }

    private float addMileage(List<TrackedRun> list){
        float sum = 0;

        for (TrackedRun tr : list){
            sum += tr.getDistanceMiles();
        }

        return sum;
    }
}
