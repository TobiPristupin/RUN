package com.example.tobias.run.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Tobi on 12/3/2017.
 */

public class GenericAxisValueFormatter<T> implements IAxisValueFormatter {

    private T[] data;

    public GenericAxisValueFormatter(T[] data) {
        this.data = data;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return data[(int) value - 1].toString();
    }
}
