package com.example.tobias.run.stats.activities;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by Tobi on 1/12/2018.
 */

public interface StatsActivitiesView {


    void setMonthChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData);

    void set3MonthsChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData);

    void set6MonthsChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData);

    void setYearChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData);
}
