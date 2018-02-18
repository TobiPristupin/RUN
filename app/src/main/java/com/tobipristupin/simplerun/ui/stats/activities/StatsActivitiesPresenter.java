package com.tobipristupin.simplerun.ui.stats.activities;

import com.tobipristupin.simplerun.data.RunPredicates;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.interfaces.Observable;
import com.tobipristupin.simplerun.interfaces.Observer;
import com.tobipristupin.simplerun.utils.DateUtils;
import com.tobipristupin.simplerun.utils.RunUtils;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tobi on 1/12/2018.
 */

public class StatsActivitiesPresenter implements Observer<List<Run>> {

    private StatsActivitiesView view;
    private Observable observable;
    private List<Run> runList = new ArrayList<>();

    public StatsActivitiesPresenter(StatsActivitiesView view, Observable observable) {
        this.view = view;
        this.observable = observable;

        this.observable.attachObserver(this);

        updateChartXLabels();
    }

    public void onDetachView(){
        this.observable.detachObserver(this);
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
        List<PieEntry> pieEntries = new ArrayList<>();

        if (allActivities == 0){
            view.setPieChartData(Collections.emptyList());
            return;
        }

        pieEntries.add(new PieEntry(getRunsFromWeekday(1) * 100 / allActivities, "Monday"));
        pieEntries.add(new PieEntry(getRunsFromWeekday(2) * 100 / allActivities, "Tuesday"));
        pieEntries.add(new PieEntry(getRunsFromWeekday(3) * 100 / allActivities, "Wednesday"));
        pieEntries.add(new PieEntry(getRunsFromWeekday(4) * 100 / allActivities, "Thursday"));
        pieEntries.add(new PieEntry(getRunsFromWeekday(5) * 100 / allActivities, "Friday"));
        pieEntries.add(new PieEntry(getRunsFromWeekday(6) * 100 / allActivities, "Saturday"));
        pieEntries.add(new PieEntry(getRunsFromWeekday(7) * 100 / allActivities, "Sunday"));


        for(int i = pieEntries.size() - 1; i >= 0; i--){
            if (pieEntries.get(i).getValue() == 0){
                pieEntries.remove(i);
            }
        }

        view.setPieChartData(pieEntries);
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

        String[] xLabelWeek = { dateTime.withDayOfWeek(1).dayOfMonth().getAsString()
                + "-" + dateTime.withDayOfWeek(7).dayOfMonth().getAsString()
                + " " + dateTime.monthOfYear().getAsShortText(Locale.getDefault()) };
        view.setGraphWeekXLabels(xLabelWeek);

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

    /**
     * @param weekDay day of week in range [1, 7]. 1 = Monday, 7 = Sunday
     * @return amount of runs that happened on weekday
     */
    private int getRunsFromWeekday(int weekDay){
        if(weekDay < 1 || weekDay > 7){
            throw new IllegalArgumentException("week day must be in range [1, 7]");
        }

        return RunUtils.filterList(runList, RunPredicates.isRunFromWeekDay(weekDay)).size();
    }
}
