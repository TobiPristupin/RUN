package com.example.tobias.run.stats.personalrecords;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by Tobi on 11/28/2017.
 */

public interface StatsPrsView {

    void set400mChartData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setMileChartData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void set5kChartData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void set10kChartData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void set21kChartData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void set42kChartData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setFarthestDistanceText(String distance, String date);

    void setLongestDurationText(String duration, String date);

    void setFastestPaceText(String pace, String date);
}
