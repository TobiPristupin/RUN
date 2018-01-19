package com.example.tobias.run.stats.activities;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.RunPredicates;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.DateUtils;
import com.example.tobias.run.utils.RunUtils;
import com.github.mikephil.charting.data.BarEntry;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tobi on 1/12/2018.
 */

public class StatsActivitiesPresenter implements Observer<List<Run>> {

    private StatsActivitiesView view;
    private ObservableDatabase<Run> model;
    private List<Run> runList = new ArrayList<>();

    public StatsActivitiesPresenter(StatsActivitiesView view, ObservableDatabase<Run> model) {
        this.view = view;
        this.model = model;

        this.model.attachObserver(this);

        updateChartXLabels();
    }

    public void onDetachView(){
        this.model.detachObserver(this);
    }

    @Override
    public void updateData(List<Run> data) {
        runList = data;
        updateWeekChartData();
        updateMonthChartData();
        update3MonthsChartData();
        update6MonthsChartData();
        updateYearChartData();
        updatePieChartData();
    }

    private void updatePieChartData(){
        int allActivities = runList.size();
        
    }

    private void updateWeekChartData(){
        int activitiesWeek = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfWeek(), DateUtils.getEndOfWeek())).size();

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, activitiesWeek));

        view.setGraphWeekData(barEntries);
    }

    private void updateMonthChartData(){
        int activitiesMonth = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth())).size();

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, activitiesMonth));

        view.setGraphMonthData(barEntries);
    }

    private void update3MonthsChartData(){
        int activities2MonthsBefore = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-2), DateUtils.getEndOfMonth(-2))).size();

        int activitiesMonthBefore = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth(-1))).size();

        int activitiesMonth = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth())).size();

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, activities2MonthsBefore));
        barEntries.add(new BarEntry(2, activitiesMonthBefore));
        barEntries.add(new BarEntry(3, activitiesMonth));

        view.setGraph3MonthsData(barEntries);
    }

    private void update6MonthsChartData(){
        int activities5To6MonthsBack = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-5), DateUtils.getEndOfMonth(-4))).size();

        int activities4To3MonthsBack = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-3), DateUtils.getEndOfMonth(-2))).size();

        int activities2To1MonthsBack = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth())).size();


        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, activities5To6MonthsBack));
        barEntries.add(new BarEntry(2, activities4To3MonthsBack));
        barEntries.add(new BarEntry(3, activities2To1MonthsBack));

        view.setGraph6MonthsData(barEntries);
    }

    private void updateYearChartData(){
        int activitiesMonths1To4 = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfYear(), DateUtils.getStartOfYearEndOfMonth(4))).size();

        int activitiesMonth4To8 = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfYearStartOfMonth(5), DateUtils.getStartOfYearEndOfMonth(8))).size();

        int activitiesMonth8To12 = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfYearStartOfMonth(9), DateUtils.getEndOfYear())).size();

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, activitiesMonths1To4));
        barEntries.add(new BarEntry(2, activitiesMonth4To8));
        barEntries.add(new BarEntry(3, activitiesMonth8To12));

        view.setGraphYearData(barEntries);
    }

    private void updateChartXLabels(){
        DateTime dateTime = new DateTime();
        Locale locale = Locale.getDefault();

        String[] xLabelsWeek = { dateTime.withDayOfWeek(1).dayOfMonth().getAsString()
                + "-" + dateTime.withDayOfWeek(7).dayOfMonth().getAsString() };
        view.setGraphWeekXLabels(xLabelsWeek);

        String[] xLabelsMonth = {dateTime.monthOfYear().getAsText(locale)};
        view.setGraphMonthXLabels(xLabelsMonth);

        String[] xLabels3Months = {
                dateTime.minusMonths(2).monthOfYear().getAsText(locale),
                dateTime.minusMonths(1).monthOfYear().getAsText(locale),
                dateTime.monthOfYear().getAsText(locale)
        };
        view.setGraph3MonthsXLabels(xLabels3Months);

        String[] xLabels6Months = {
                dateTime.minusMonths(5).monthOfYear().getAsShortText(locale) + "-" + dateTime.minusMonths(4).monthOfYear().getAsShortText(locale),
                dateTime.minusMonths(3).monthOfYear().getAsShortText(locale) + "-" + dateTime.minusMonths(2).monthOfYear().getAsShortText(locale),
                dateTime.minusMonths(1).monthOfYear().getAsShortText(locale) + "-" + dateTime.monthOfYear().getAsShortText(locale)
        };
        view.setGraph6MonthsXLabels(xLabels6Months);

        String[] xLabelsYear = {
                dateTime.withMonthOfYear(1).monthOfYear().getAsShortText(locale) + "-" + dateTime.withMonthOfYear(4).monthOfYear().getAsShortText(locale),
                dateTime.withMonthOfYear(5).monthOfYear().getAsShortText(locale) + "-" + dateTime.withMonthOfYear(8).monthOfYear().getAsShortText(locale),
                dateTime.withMonthOfYear(9).monthOfYear().getAsShortText(locale) + "-" + dateTime.withMonthOfYear(12).monthOfYear().getAsShortText(locale),
        };
        view.setGraphYearXLabels(xLabelsYear);
    }
}
