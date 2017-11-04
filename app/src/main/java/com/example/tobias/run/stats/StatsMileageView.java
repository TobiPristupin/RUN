package com.example.tobias.run.stats;

import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Created by Tobi on 10/29/2017.
 */

public interface StatsMileageView {

    void setGraphMonthData(List<BarEntry> data);

    void setGraph3MonthData(List<BarEntry> data);

    void setGraph6MonthData(List<BarEntry> data);

    void setGraphYearData(List<BarEntry> data);

    void setGraphMonthXLabel(String[] values);

    void setGraph3MonthXLabel(String[] values);

    void setGraph6MonthXLabel(String[] values);

    void setGraphYearXLabel(String[] values);
}
