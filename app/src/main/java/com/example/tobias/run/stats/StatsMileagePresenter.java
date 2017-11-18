package com.example.tobias.run.stats;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.DateUtils;
import com.example.tobias.run.utils.TrackedRunUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tobi on 10/30/2017.
 */

public class StatsMileagePresenter implements Observer<List<TrackedRun>> {

    private StatsMileageView view;
    private ObservableDatabase model;
    private SharedPreferenceRepository sharedPrefRepository;
    private List<TrackedRun> trackedRunList = new ArrayList<>();

    public StatsMileagePresenter(StatsMileageView view, ObservableDatabase<TrackedRun> model, SharedPreferenceRepository sharedPrefRepository) {
        this.view = view;
        this.model = model;
        this.sharedPrefRepository = sharedPrefRepository;

        this.model.attachObserver(this);
        this.model.startQuery();
    }

    @Override
    public void updateData(List<TrackedRun> data) {
        trackedRunList = data;
        updateBarChartMonth();
        updateBarChart3Month();
        updateBarChart6Months();
        updateBarChartYear();
        updateTotalDistanceText();
    }

    public void onDetachView(){
        model.detachObserver(this);
    }

    public void updateTotalDistanceText(){
        String mileageMonth = (int) getMonthMileage() + " " + getDistanceUnit();
        view.setTotalDistanceMonth(mileageMonth);

        String mileage3Months = (int) TrackedRunUtils.addMileage(get3MonthsMileage()) + " " + getDistanceUnit();
        view.setTotalDistance3Months(mileage3Months);

        String mileage6Months = (int) TrackedRunUtils.addMileage(get6MonthsMileage()) + " " + getDistanceUnit();
        view.setTotalDistance6Months(mileage6Months);

        String mileageYear = (int) TrackedRunUtils.addMileage(getYearMileage()) + " " + getDistanceUnit();
        view.setTotalDistanceYear(mileageYear);
    }

    private void updateBarChartMonth(){

        float mileageSum = getMonthMileage();

        if (mileageSum != 0){
            Map<Integer, Float> barEntries = putValuesIntoHashMap(mileageSum);
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
            Map<Integer, Float> barEntries = putValuesIntoHashMap(mileage2MonthsPrevious, mileagePreviousMonth, mileageMonth);
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
            Map<Integer, Float> barEntries = putValuesIntoHashMap(mileageMonths1To2, mileageMonths3To4, mileageMonths5To6);
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
            Map<Integer, Float> barEntries = putValuesIntoHashMap(mileageMonths1To4, mileageMonths4To8, mileageMonths8To12);
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
        float mileageSum = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfCurrentMonth(),
                DateUtils.getEndOfCurrentMonth(), trackedRunList, sharedPrefRepository);

        return mileageSum;
    }

    private float[] get3MonthsMileage(){
        float mileage2MonthsPrevious = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfMonthMinusMonths(2),
                DateUtils.getEndOfMonthMinusMonths(2), trackedRunList, sharedPrefRepository);

        float mileagePreviousMonth = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfMonthMinusMonths(1),
                DateUtils.getEndOfMonthMinusMonths(1), trackedRunList, sharedPrefRepository);

        float mileageMonth = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfCurrentMonth(),
                DateUtils.getEndOfCurrentMonth(), trackedRunList, sharedPrefRepository);

        return new float[] {mileage2MonthsPrevious, mileagePreviousMonth, mileageMonth};
    }

    private float[] get6MonthsMileage(){
        float mileageMonths1To2 = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfMonthMinusMonths(5),
                DateUtils.getEndOfMonthMinusMonths(4), trackedRunList, sharedPrefRepository);

        float mileageMonths3To4 = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfMonthMinusMonths(3),
                DateUtils.getEndOfMonthMinusMonths(2), trackedRunList, sharedPrefRepository);

        float mileageMonths5To6 = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfMonthMinusMonths(1),
                DateUtils.getEndOfCurrentMonth(), trackedRunList, sharedPrefRepository);

        return new float[] {mileageMonths1To2, mileageMonths3To4, mileageMonths5To6};
    }

    private float[] getYearMileage(){
        float mileageMonths1To4 = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfYear(),
                DateUtils.getStartOfYearPlusMonths(4), trackedRunList, sharedPrefRepository);

        float mileageMonths4To8 = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfYearPlusMonths(4),
                DateUtils.getStartOfYearPlusMonths(8), trackedRunList, sharedPrefRepository);

        float mileageMonths8To12 = TrackedRunUtils.getMileageBetween(DateUtils.getStartOfYearPlusMonths(8),
                DateUtils.getEndOfYear(), trackedRunList, sharedPrefRepository);

        return new float[] {mileageMonths1To4, mileageMonths4To8, mileageMonths8To12};
    }

    /**
     * Note: The order parameters are passed will determine their key values.
     * @param mileage float value of mileage in a given month
     * @return Map with keys for x axis and values for y axis
     */
    private Map<Integer, Float> putValuesIntoHashMap(float... mileage){
        Map<Integer, Float> map = new HashMap<>();
        int counter = 1;

        for(float j : mileage){
            map.put(counter, j);
            counter++;
        }


        return map;
    }

    private String getDistanceUnit(){
        return sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
    }


}
