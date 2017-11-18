package com.example.tobias.run.stats;

import java.util.Map;

/**
 * Created by Tobi on 10/29/2017.
 */

public interface StatsMileageView {

    void setGraphMonthData(Map<Integer, Float> data);

    void setGraph3MonthData(Map<Integer, Float> data);

    void setGraph6MonthData(Map<Integer, Float> data);

    void setGraphYearData(Map<Integer, Float> data);

    void setGraphMonthXLabel(String[] values);

    void setGraph3MonthXLabel(String[] values);

    void setGraph6MonthXLabel(String[] values);

    void setGraphYearXLabel(String[] values);

    void setTotalDistanceMonth(String text);

    void setTotalDistance3Months(String text);

    void setTotalDistance6Months(String text);

    void setTotalDistanceYear(String text);
}
