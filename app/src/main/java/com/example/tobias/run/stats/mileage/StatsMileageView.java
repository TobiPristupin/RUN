package com.example.tobias.run.stats.mileage;

import com.example.tobias.run.utils.StateChange;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Created by Tobi on 10/29/2017.
 */

public interface StatsMileageView {

    void setGraphMonthData(List<BarEntry> barEntries);

    void setGraph3MonthData(List<BarEntry> barEntries);

    void setGraph6MonthData(List<BarEntry> barEntries);

    void setGraphYearData(List<BarEntry> barEntries);

    void setGraphMonthXLabel(String[] values);

    void setGraph3MonthXLabel(String[] values);

    void setGraph6MonthXLabel(String[] values);

    void setGraphYearXLabel(String[] values);

    void setTotalDistanceMonth(String text);

    void setTotalDistance3Months(String text);

    void setTotalDistance6Months(String text);

    void setTotalDistanceYear(String text);

    void setMonthIncreaseText(String text, StateChange change);

    void set3MonthsIncreaseText(String text, StateChange change);

    void set6MonthsIncreaseText(String text, StateChange change);

    void setYearIncreaseText(String text, StateChange change);

}
