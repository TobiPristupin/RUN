package com.tobipristupin.simplerun.ui.stats.mileage;

import com.github.mikephil.charting.data.BarEntry;
import com.tobipristupin.simplerun.data.RunPredicates;
import com.tobipristupin.simplerun.data.repository.PreferencesRepository;
import com.tobipristupin.simplerun.data.model.DistanceUnit;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.data.repository.Repository;
import com.tobipristupin.simplerun.utils.DateUtils;
import com.tobipristupin.simplerun.utils.RunUtils;
import com.tobipristupin.simplerun.utils.State;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class StatsMileagePresenter {

    private StatsMileageView view;
    private List<Run> runList = new ArrayList<>();
    private Repository<Run> runRepository;
    private PreferencesRepository preferencesRepository;
    private Disposable repositorySubscription;

    public StatsMileagePresenter(StatsMileageView view, Repository<Run> runRepository, PreferencesRepository preferencesRepository) {
        this.view = view;
        this.runRepository = runRepository;
        this.preferencesRepository = preferencesRepository;

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
        updateBarChartWeek();
        updateBarChartMonth();
        updateBarChart3Month();
        updateBarChart6Months();
        updateBarChartYear();
        updateTotalDistanceText();
        updateIncreaseText();
    }

    public void onDetachView(){
        if (repositorySubscription != null){
            repositorySubscription.dispose();
        }
    }

    private void updateChartXLabels(){
        DateTime dateTime = new DateTime();

        //Format {day number}-{day number} {month short text} e.g. 12-19 Feb
        String[] xLabelWeek = {
                DateUtils.getDayOfMonthString(dateTime.withDayOfWeek(1))
                        + "-" + DateUtils.getDayOfMonthString(dateTime.withDayOfWeek(7))
                        + " " + DateUtils.getMonthShortText(dateTime)
        };
        view.setGraphWeekXLabel(xLabelWeek);

        //Format {month short text} e.g. Feb
        String xLabelsMonth = DateUtils.getMonthShortText(dateTime);
        view.setGraphMonthXLabel(new String[]{xLabelsMonth});

        //Format {month text} e.g. February
        String[] xLabels3Months = {
                DateUtils.getMonthText(dateTime.minusMonths(2)),
                DateUtils.getMonthText(dateTime.minusMonths(1)),
                DateUtils.getMonthText(dateTime),
        };
        view.setGraph3MonthXLabel(xLabels3Months);

        //Format {month short text}-{month short text} e.g. Jan-Feb
        String[] xLabels6Months = {
                DateUtils.getMonthShortText(dateTime.minusMonths(5)) + "-" + DateUtils.getMonthShortText(dateTime.minusMonths(4)),
                DateUtils.getMonthShortText(dateTime.minusMonths(3)) + "-" + DateUtils.getMonthShortText(dateTime.minusMonths(2)),
                DateUtils.getMonthShortText(dateTime.minusMonths(1)) + "-" + DateUtils.getMonthShortText(dateTime)
        };
        view.setGraph6MonthXLabel(xLabels6Months);

        //Format {month short text}-{month short text} e.g. Jan-Feb
        String[] xLabelsYear = {
                DateUtils.getMonthShortText(dateTime.withMonthOfYear(1)) + "-" + DateUtils.getMonthShortText(dateTime.withMonthOfYear(4)),
                DateUtils.getMonthShortText(dateTime.withMonthOfYear(5)) + "-" + DateUtils.getMonthShortText(dateTime.withMonthOfYear(8)),
                DateUtils.getMonthShortText(dateTime.withMonthOfYear(9)) + "-" + DateUtils.getMonthShortText(dateTime.withMonthOfYear(12)),
        };

        view.setGraphYearXLabel(xLabelsYear);
    }

    private void updateTotalDistanceText(){
        DecimalFormat df = getDecimalFormat();

        String mileageWeek = df.format(getWeekMileage()) + " " + getDistanceUnit();
        view.setTotalDistanceWeek(mileageWeek);

        String mileageMonth =  df.format(getMonthMileage()) + " " + getDistanceUnit();
        view.setTotalDistanceMonth(mileageMonth);

        String mileage3Months =  df.format(RunUtils.addArray(get3MonthsMileage())) + " " + getDistanceUnit();
        view.setTotalDistance3Months(mileage3Months);

        String mileage6Months =  df.format(RunUtils.addArray(get6MonthsMileage())) + " " + getDistanceUnit();
        view.setTotalDistance6Months(mileage6Months);

        String mileageYear = df.format(RunUtils.addArray(getYearMileage())) + " " + getDistanceUnit();
        view.setTotalDistanceYear(mileageYear);
    }

    private void updateIncreaseText(){
        DecimalFormat df = getDecimalFormat();

        /*When passing the increase string to the view, there is no need to prefix it with '-' when
        the value is negative because the number will already contain it*/

        double monthIncrease = getMonthMileage() - getPastMonthMileageTotal();
        if (monthIncrease >= 0){
            view.setMonthIncreaseText("+" + df.format(monthIncrease), State.INCREASE);
        } else {
            view.setMonthIncreaseText(df.format(monthIncrease), State.DECREASE);
        }

        double months3Increase = RunUtils.addArray(get3MonthsMileage()) - getPast3MonthsMileageTotal();
        if (months3Increase >= 0){
            view.set3MonthsIncreaseText("+" + df.format(months3Increase), State.INCREASE);
        } else {
            view.set3MonthsIncreaseText(df.format(months3Increase), State.DECREASE);
        }

        double months6Increase = RunUtils.addArray(get6MonthsMileage()) - getPast6MonthsMileageTotal();
        if (months6Increase >= 0){
            view.set6MonthsIncreaseText("+" + df.format(months6Increase), State.INCREASE);
        } else {
            view.set6MonthsIncreaseText(df.format(months6Increase), State.DECREASE);
        }

        double yearIncrease = RunUtils.addArray(getYearMileage()) - getPastYearMileage();
        if (yearIncrease >= 0){
            view.setYearIncreaseText("+" + df.format(yearIncrease), State.INCREASE);
        } else {
            view.setYearIncreaseText(df.format(yearIncrease), State.DECREASE);
        }
    }

    private void updateBarChartWeek(){
        double mileageSum = getWeekMileage();

        if (mileageSum != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, (float) mileageSum));
            view.setGraphWeekData(barEntries);
        }
    }

    private void updateBarChartMonth(){

        double mileageSum = getMonthMileage();

        if (mileageSum != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, (float) mileageSum));
            view.setGraphMonthData(barEntries);
        }
    }

    private void updateBarChart3Month(){
        double mileage2MonthsPrevious = get3MonthsMileage()[0];

        double mileagePreviousMonth = get3MonthsMileage()[1];

        double mileageMonth = get3MonthsMileage()[2];

        if (mileage2MonthsPrevious + mileagePreviousMonth + mileageMonth != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, (float) mileage2MonthsPrevious));
            barEntries.add(new BarEntry(2, (float) mileagePreviousMonth));
            barEntries.add(new BarEntry(3, (float) mileageMonth));
            view.setGraph3MonthData(barEntries);
        }
    }

    private void updateBarChart6Months(){
        double mileageMonths1To2 = get6MonthsMileage()[0];

        double mileageMonths3To4 = get6MonthsMileage()[1];

        double mileageMonths5To6 = get6MonthsMileage()[2];
        if (mileageMonths1To2 + mileageMonths3To4 + mileageMonths5To6 != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, (float) mileageMonths1To2));
            barEntries.add(new BarEntry(2, (float) mileageMonths3To4));
            barEntries.add(new BarEntry(3, (float) mileageMonths5To6));
            view.setGraph6MonthData(barEntries);
        }
    }

    private void updateBarChartYear(){
        double mileageMonths1To4 = getYearMileage()[0];

        double mileageMonths4To8 = getYearMileage()[1];

        double mileageMonths8To12 = getYearMileage()[2];

        if (mileageMonths1To4 + mileageMonths4To8 + mileageMonths8To12 != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, (float) mileageMonths1To4));
            barEntries.add(new BarEntry(2, (float) mileageMonths4To8));
            barEntries.add(new BarEntry(3, (float) mileageMonths8To12));
            view.setGraphYearData(barEntries);
        }
    }

    private double getWeekMileage(){
        List<Run> weekRuns = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfWeek(), DateUtils.getEndOfWeek()));

        double mileageSum = RunUtils.addMileage(weekRuns, getDistanceUnit());

        return mileageSum;
    }

    private double getMonthMileage(){
        List<Run> monthRuns = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth()));

        double mileageSum = RunUtils.addMileage(monthRuns, getDistanceUnit());

        return mileageSum;
    }

    private double getPastMonthMileageTotal(){
        List<Run> lastMonthRuns = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth(-1)));

        double mileageSum = RunUtils.addMileage(lastMonthRuns, getDistanceUnit());

        return mileageSum;
    }

    private double[] get3MonthsMileage(){
        List<Run> runs2MonthsBack = RunUtils.filterList(runList, RunPredicates.
                isRunBetween(DateUtils.getStartOfMonth(-2), DateUtils.getEndOfMonth(-2)));

        List<Run> runsLastMonth = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth(-1)));

        List<Run> monthRuns = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth()));


        double mileage2MonthsPrevious = RunUtils.addMileage(runs2MonthsBack, getDistanceUnit());
        double mileagePreviousMonth = RunUtils.addMileage(runsLastMonth, getDistanceUnit());
        double mileageMonth = RunUtils.addMileage(monthRuns, getDistanceUnit());

        return new double[] {mileage2MonthsPrevious, mileagePreviousMonth, mileageMonth};
    }

    private double getPast3MonthsMileageTotal(){
        List<Run> last3MonthsRuns = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-5), DateUtils.getEndOfMonth(-3)));

        double mileageSum = RunUtils.addMileage(last3MonthsRuns, getDistanceUnit());

        return mileageSum;
    }

    private double[] get6MonthsMileage(){
        List<Run> runs5To6MonthsBack = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-5), DateUtils.getEndOfMonth(-4)));

        List<Run> runs4To3MonthsBack = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-3), DateUtils.getEndOfMonth(-2)));

        List<Run> runs2To1MonthsBack = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth()));

        double mileageMonths1To2 = RunUtils.addMileage(runs5To6MonthsBack, getDistanceUnit());

        double mileageMonths3To4 = RunUtils.addMileage(runs4To3MonthsBack, getDistanceUnit());

        double mileageMonths5To6 = RunUtils.addMileage(runs2To1MonthsBack, getDistanceUnit());

        return new double[] {mileageMonths1To2, mileageMonths3To4, mileageMonths5To6};
    }

    private double getPast6MonthsMileageTotal(){
        List<Run> runsLast6Months = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfMonth(-11), DateUtils.getEndOfMonth(-6)));

        double mileageSum = RunUtils.addMileage(runsLast6Months, getDistanceUnit());

        return mileageSum;
    }

    private double getPastYearMileage(){
        List<Run> runsLastYear = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfLastYear(),  DateUtils.getEndOfLastYear()));

        double mileageSum = RunUtils.addMileage(runsLastYear, getDistanceUnit());

        return mileageSum;
    }

    private double[] getYearMileage(){
        List<Run> runsMonths1To4 = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfYear(), DateUtils.getStartOfYearEndOfMonth(4)));

        List<Run> runsMonth4To8 = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfYearStartOfMonth(5), DateUtils.getStartOfYearEndOfMonth(8)));

        List<Run> runsMonth8To12 = RunUtils.filterList(runList,
                RunPredicates.isRunBetween(DateUtils.getStartOfYearStartOfMonth(9), DateUtils.getEndOfYear()));

        double mileageMonths1To4 = RunUtils.addMileage(runsMonths1To4, getDistanceUnit());

        double mileageMonths4To8 = RunUtils.addMileage(runsMonth4To8, getDistanceUnit());

        double mileageMonths8To12 = RunUtils.addMileage(runsMonth8To12, getDistanceUnit());

        return new double[] {mileageMonths1To4, mileageMonths4To8, mileageMonths8To12};
    }


    private DistanceUnit getDistanceUnit(){
        return preferencesRepository.getDistanceUnit();
    }

    private DecimalFormat getDecimalFormat(){
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(Locale.getDefault());
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#.#");
        return df;
    }


}