package com.example.tobias.run.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Axis value formatter for graphs
 */

public class AxisValueFormatter implements IAxisValueFormatter {

    private String[] values;

    public AxisValueFormatter(String[] values){
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values[(int) value - 1];
    }


}
