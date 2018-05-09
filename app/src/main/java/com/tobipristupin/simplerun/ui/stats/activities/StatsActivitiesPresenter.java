package com.tobipristupin.simplerun.ui.stats.activities;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.tobipristupin.simplerun.data.RunPredicates;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.data.repository.Repository;
import com.tobipristupin.simplerun.utils.DateUtils;
import com.tobipristupin.simplerun.utils.RunUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class StatsActivitiesPresenter {

    private StatsActivitiesView view;
    private List<Run> runList = new ArrayList<>();
    private Repository<Run> runRepository;
    private Disposable repositorySubscription;

    public StatsActivitiesPresenter(StatsActivitiesView view, Repository<Run> runRepository) {
        this.view = view;
        this.runRepository = runRepository;

        updateChartXLabels();
        subscribeToData();
    }

    private void subscribeToData(){
        repositorySubscription = runRepository.fetch().subscribeWith(new DisposableObserver<List<Run>>(){

            @Override
            public void onNext(List<Run> runs) {
                onNewData(runs);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void onNewData(List<Run> data){
        runList = data;
        updateWeekChartData();
        updateMonthChartData();
        update3MonthsChartData();
        update6MonthsChartData();
        updateYearChartData();
        updatePieChartData();
    }

    public void onDetachView(){
        if (repositorySubscription != null){
            repositorySubscription.dispose();
        }
    }

    private void updatePieChartData(){
        int allActivities = runList.size();
        List<PieEntry> pieEntries = new ArrayList<>();

        if (allActivities == 0){
            view.setPieChartData(Collections.emptyList());
            return;
        }

        pieEntries.add(new PieEntry(getRunsFromWeekday(1) * 100 / allActivities ));
        pieEntries.add(new PieEntry(getRunsFromWeekday(2) * 100 / allActivities));
        pieEntries.add(new PieEntry(getRunsFromWeekday(3) * 100 / allActivities));
        pieEntries.add(new PieEntry(getRunsFromWeekday(4) * 100 / allActivities));
        pieEntries.add(new PieEntry(getRunsFromWeekday(5) * 100 / allActivities));
        pieEntries.add(new PieEntry(getRunsFromWeekday(6) * 100 / allActivities));
        pieEntries.add(new PieEntry(getRunsFromWeekday(7) * 100 / allActivities));


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

        //Format {day of month}-{day of month} {month short text} e.g. 19-26 Feb
        String[] xLabelWeek = {
                DateUtils.getDayOfMonthString(dateTime.withDayOfWeek(1))
                + "-" + DateUtils.getDayOfMonthString(dateTime.withDayOfWeek(7))
                + " " + DateUtils.getMonthShortText(dateTime)};
        view.setGraphWeekXLabels(xLabelWeek);

        String[] xLabelsMonth = {DateUtils.getMonthText(dateTime)};
        view.setGraphMonthXLabels(xLabelsMonth);

        //Format {month} e.g. February
        String[] xLabels3Months = {
                DateUtils.getMonthText(dateTime.minusMonths(2)),
                DateUtils.getMonthText(dateTime.minusMonths(1)),
                DateUtils.getMonthText(dateTime)
        };
        view.setGraph3MonthsXLabels(xLabels3Months);

        //Format {month short text}-{month short text} e.g Jan-Feb
        String[] xLabels6Months = {
                DateUtils.getMonthShortText(dateTime.minusMonths(5)) + "-" + DateUtils.getMonthShortText(dateTime.minusMonths(4)),
                DateUtils.getMonthShortText(dateTime.minusMonths(3)) + "-" + DateUtils.getMonthShortText(dateTime.minusMonths(2)),
                DateUtils.getMonthShortText(dateTime.minusMonths(1)) + "-" + DateUtils.getMonthShortText(dateTime)
        };
        view.setGraph6MonthsXLabels(xLabels6Months);

        //Format {month short text}-{month short text} e.g Jan-Feb
        String[] xLabelsYear = {
                DateUtils.getMonthShortText(dateTime.withMonthOfYear(1)) + "-" + DateUtils.getMonthShortText(dateTime.withMonthOfYear(4)),
                DateUtils.getMonthShortText(dateTime.withMonthOfYear(5)) + "-" + DateUtils.getMonthShortText(dateTime.withMonthOfYear(8)),
                DateUtils.getMonthShortText(dateTime.withMonthOfYear(9)) + "-" + DateUtils.getMonthShortText(dateTime.withMonthOfYear(12)),
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
