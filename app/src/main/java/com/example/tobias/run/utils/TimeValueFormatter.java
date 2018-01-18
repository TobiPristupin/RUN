package com.example.tobias.run.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Tobi on 12/10/2017.
 */

public class TimeValueFormatter implements IAxisValueFormatter, IValueFormatter{

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return RunUtils.timeToString((long) value, false);
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return RunUtils.timeToString((long) value, false);
    }
}
