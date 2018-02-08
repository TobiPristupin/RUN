package com.example.tobias.run.ui.stats.personalrecords;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by Tobi on 11/28/2017.
 */

public interface StatsPrsView {

    void setGraph400mData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setGraphMileData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setGraph5kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setGraph10kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setGraph21kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setGraph42kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage);

    void setFarthestDistanceText(String distance, String date);

    void setLongestDurationText(String duration, String date);

    void setFastestPaceText(String pace, String date);
}
