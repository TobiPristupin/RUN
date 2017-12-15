package com.example.tobias.run.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Tobi on 12/10/2017.
 */

public class TimeAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return RunUtils.timeToString((long) value, false);
    }
}
