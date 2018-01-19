package com.example.tobias.run.stats.activities;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

/**
 * Created by Tobi on 1/12/2018.
 */

public interface StatsActivitiesView {

    void setGraphWeekData(List<BarEntry> barData);

    void setGraphMonthData(List<BarEntry> barData);

    void setGraph3MonthsData(List<BarEntry> barData);

    void setGraph6MonthsData(List<BarEntry> barData);

    void setGraphYearData(List<BarEntry> barData);
    
    void setGraphWeekXLabels(String[] labels);

    void setGraphMonthXLabels(String[] labels);

    void setGraph3MonthsXLabels(String[] labels);

    void setGraph6MonthsXLabels(String[] labels);

    void setGraphYearXLabels(String[] labels);

    void setPieChartData(List<PieEntry> data);
}
