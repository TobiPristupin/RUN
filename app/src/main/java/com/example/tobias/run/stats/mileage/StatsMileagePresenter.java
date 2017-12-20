package com.example.tobias.run.stats.mileage;

import com.example.tobias.run.data.Distance;
import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.DateUtils;
import com.example.tobias.run.utils.RunUtils;
import com.github.mikephil.charting.data.BarEntry;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 10/30/2017.
 */

public class StatsMileagePresenter implements Observer<List<Run>> {

    private StatsMileageView view;
    private ObservableDatabase model;
    private SharedPreferenceRepository sharedPrefRepository;
    private List<Run> runList = new ArrayList<>();

    public StatsMileagePresenter(StatsMileageView view, ObservableDatabase<Run> model, SharedPreferenceRepository sharedPrefRepository) {
        this.view = view;
        this.model = model;
        this.sharedPrefRepository = sharedPrefRepository;

        this.model.attachObserver(this);
    }

    @Override
    public void updateData(List<Run> data) {
        runList = data;
        updateBarChartMonth();
        updateBarChart3Month();
        updateBarChart6Months();
        updateBarChartYear();
        updateTotalDistanceText();
        updateIncreaseText();
    }

    public void onDetachView(){
        model.detachObserver(this);
    }

    private void updateTotalDistanceText(){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        String mileageMonth =  df.format(getMonthMileage()) + " " + getDistanceUnit();
        view.setTotalDistanceMonth(mileageMonth);

        String mileage3Months =  df.format(RunUtils.addMileage(get3MonthsMileage())) + " " + getDistanceUnit();
        view.setTotalDistance3Months(mileage3Months);

        String mileage6Months =  df.format(RunUtils.addMileage(get6MonthsMileage())) + " " + getDistanceUnit();
        view.setTotalDistance6Months(mileage6Months);

        String mileageYear = df.format(RunUtils.addMileage(getYearMileage())) + " " + getDistanceUnit();
        view.setTotalDistanceYear(mileageYear);
    }

    private void updateIncreaseText(){
        float monthIncrease = getMonthMileage() - getPastMonthMileageTotal();
        if (monthIncrease >= 0){
            view.setMonthIncreaseText("+" + String.format("%.1f", monthIncrease), true);
        } else {
            view.setMonthIncreaseText(String.format("%.1f", monthIncrease), false);
        }

        float months3Increase = RunUtils.addMileage(get3MonthsMileage()) - getPast3MonthsMileageTotal();
        System.out.println(months3Increase);
        if (months3Increase >= 0){
            view.set3MonthsIncreaseText("+" + String.format("%.1f", months3Increase), true);
        } else {
            view.set3MonthsIncreaseText(String.format("%.1f", months3Increase), false);
        }

        float months6Increase = RunUtils.addMileage(get6MonthsMileage()) - getPast6MonthsMileageTotal();
        if (months6Increase >= 0){
            view.set6MonthsIncreaseText("+" + String.format("%.1f", months6Increase), true);
        } else {
            view.set6MonthsIncreaseText(String.format("%.1f", months6Increase), false);
        }

        float yearIncrease = RunUtils.addMileage(getYearMileage()) - getPastYearMileage();
        if (yearIncrease >= 0){
            view.setYearIncreaseText("+" + String.format("%.1f", yearIncrease), true);
        } else {
            view.setYearIncreaseText(String.format("%.1f", yearIncrease), false);
        }
    }

    private void updateBarChartMonth(){

        float mileageSum = getMonthMileage();

        if (mileageSum != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, mileageSum));
            view.setGraphMonthData(barEntries);
        }

        DateTime dateTime = new DateTime();
        String monthStr = dateTime.monthOfYear().getAsText();
        view.setGraphMonthXLabel(new String[]{monthStr});
    }

    private void updateBarChart3Month(){
        float mileage2MonthsPrevious = get3MonthsMileage()[0];

        float mileagePreviousMonth = get3MonthsMileage()[1];

        float mileageMonth = get3MonthsMileage()[2];

        if (mileage2MonthsPrevious + mileagePreviousMonth + mileageMonth != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, mileage2MonthsPrevious));
            barEntries.add(new BarEntry(2, mileagePreviousMonth));
            barEntries.add(new BarEntry(3, mileageMonth));
            view.setGraph3MonthData(barEntries);
        }

        DateTime dateTime = new DateTime();
        String[] xAxisLabelValues = {
                dateTime.minusMonths(2).monthOfYear().getAsText(),
                dateTime.minusMonths(1).monthOfYear().getAsText(),
                dateTime.monthOfYear().getAsText(),
        };
        view.setGraph3MonthXLabel(xAxisLabelValues);
    }

    private void updateBarChart6Months(){
        float mileageMonths1To2 = get6MonthsMileage()[0];

        float mileageMonths3To4 = get6MonthsMileage()[1];

        float mileageMonths5To6 = get6MonthsMileage()[2];
        if (mileageMonths1To2 + mileageMonths3To4 + mileageMonths5To6 != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, mileageMonths1To2));
            barEntries.add(new BarEntry(2, mileageMonths3To4));
            barEntries.add(new BarEntry(3, mileageMonths5To6));
            view.setGraph6MonthData(barEntries);
        }

        DateTime dateTime = new DateTime();
        String[] xAxisLabelValues = {
                dateTime.minusMonths(5).monthOfYear().getAsShortText() + "-" + dateTime.minusMonths(4).monthOfYear().getAsShortText(),
                dateTime.minusMonths(3).monthOfYear().getAsShortText() + "-" + dateTime.minusMonths(2).monthOfYear().getAsShortText(),
                dateTime.minusMonths(1).monthOfYear().getAsShortText() + "-" + dateTime.monthOfYear().getAsShortText()
        };

        view.setGraph6MonthXLabel(xAxisLabelValues);
    }

    private void updateBarChartYear(){
        float mileageMonths1To4 = getYearMileage()[0];

        float mileageMonths4To8 = getYearMileage()[1];

        float mileageMonths8To12 = getYearMileage()[2];

        if (mileageMonths1To4 + mileageMonths4To8 + mileageMonths8To12 != 0){
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, mileageMonths1To4));
            barEntries.add(new BarEntry(2, mileageMonths4To8));
            barEntries.add(new BarEntry(3, mileageMonths8To12));
            view.setGraphYearData(barEntries);
        }

        DateTime dateTime = new DateTime();
        String[] xAxisLabelValues = {
                dateTime.withMonthOfYear(1).monthOfYear().getAsShortText() + "-" + dateTime.withMonthOfYear(4).monthOfYear().getAsShortText(),
                dateTime.withMonthOfYear(5).monthOfYear().getAsShortText() + "-" + dateTime.withMonthOfYear(8).monthOfYear().getAsShortText(),
                dateTime.withMonthOfYear(9).monthOfYear().getAsShortText() + "-" + dateTime.withMonthOfYear(12).monthOfYear().getAsShortText(),
        };

        view.setGraphYearXLabel(xAxisLabelValues);
    }

    private float getMonthMileage(){
        List<Run> monthRuns = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth()));

        float mileageSum = RunUtils.addMileage(monthRuns, getDistanceUnit());

        return mileageSum;
    }

    private float getPastMonthMileageTotal(){
        List<Run> lastMonthRuns = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth(-1)));

        float mileageSum = RunUtils.addMileage(lastMonthRuns, getDistanceUnit());

        return mileageSum;
    }

    private float[] get3MonthsMileage(){
        List<Run> runs2MonthsBack = RunUtils.filterList(runList, Run.Predicates.
                isRunBetween(DateUtils.getStartOfMonth(-2), DateUtils.getEndOfMonth(-2)));

        List<Run> runsLastMonth = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth(-1)));

        List<Run> monthRuns = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth()));


        float mileage2MonthsPrevious = RunUtils.addMileage(runs2MonthsBack, getDistanceUnit());
        float mileagePreviousMonth = RunUtils.addMileage(runsLastMonth, getDistanceUnit());
        float mileageMonth = RunUtils.addMileage(monthRuns, getDistanceUnit());

        return new float[] {mileage2MonthsPrevious, mileagePreviousMonth, mileageMonth};
    }

    private float getPast3MonthsMileageTotal(){
        List<Run> last3MonthsRuns = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(-5), DateUtils.getEndOfMonth(-3)));

        float mileageSum = RunUtils.addMileage(last3MonthsRuns, getDistanceUnit());

        return mileageSum;
    }

    private float[] get6MonthsMileage(){
        List<Run> runs5To6MonthsBack = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(-5), DateUtils.getEndOfMonth(-4)));

        List<Run> runs4To3MonthsBack = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(-3), DateUtils.getEndOfMonth(-2)));

        List<Run> runs2To1MonthsBack = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(-1), DateUtils.getEndOfMonth()));

        float mileageMonths1To2 = RunUtils.addMileage(runs5To6MonthsBack, getDistanceUnit());

        float mileageMonths3To4 = RunUtils.addMileage(runs4To3MonthsBack, getDistanceUnit());

        float mileageMonths5To6 = RunUtils.addMileage(runs2To1MonthsBack, getDistanceUnit());

        return new float[] {mileageMonths1To2, mileageMonths3To4, mileageMonths5To6};
    }

    private float getPast6MonthsMileageTotal(){
        List<Run> runsLast6Months = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfMonth(-11), DateUtils.getEndOfMonth(-6)));

        float mileageSum = RunUtils.addMileage(runsLast6Months, getDistanceUnit());

        return mileageSum;
    }

    private float getPastYearMileage(){
        List<Run> runsLastYear = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfLastYear(),  DateUtils.getEndOfLastYear()));

        float mileageSum = RunUtils.addMileage(runsLastYear, getDistanceUnit());

        return mileageSum;
    }

    private float[] getYearMileage(){
        List<Run> runsMonths1To4 = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfYear(), DateUtils.getStartOfYearEndOfMonth(4)));

        List<Run> runsMonth4To8 = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfYearStartOfMonth(5), DateUtils.getStartOfYearEndOfMonth(8)));

        List<Run> runsMonth8To12 = RunUtils.filterList(runList,
                Run.Predicates.isRunBetween(DateUtils.getStartOfYearStartOfMonth(9), DateUtils.getEndOfYear()));

        float mileageMonths1To4 = RunUtils.getMileageBetween(DateUtils.getStartOfYear(),
                DateUtils.getStartOfYearEndOfMonth(4), runList, getDistanceUnit());

        float mileageMonths4To8 = RunUtils.getMileageBetween(DateUtils.getStartOfYearStartOfMonth(5),
                DateUtils.getStartOfYearEndOfMonth(8), runList, getDistanceUnit());

        float mileageMonths8To12 = RunUtils.getMileageBetween(DateUtils.getStartOfYearStartOfMonth(9),
                DateUtils.getEndOfYear(), runList, getDistanceUnit());

        return new float[] {mileageMonths1To4, mileageMonths4To8, mileageMonths8To12};
    }


    private Distance.Unit getDistanceUnit(){
        return sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
    }


}