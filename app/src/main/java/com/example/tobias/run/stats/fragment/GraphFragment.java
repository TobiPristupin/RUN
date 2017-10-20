package com.example.tobias.run.stats.fragment;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by Tobi on 10/15/2017.
 */

public interface GraphFragment {
    void setDataSet(List<Entry> dataSet);
}
