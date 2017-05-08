package com.example.tobias.run;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Stats fragment that shows mileage
 */

public class StatsMileageFragment extends Fragment{

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_mileage, container, false);
        initMpGraph();
        return rootView;
    }

    private void initMpGraph() {
        BarChart barChart = (BarChart) rootView.findViewById(R.id.mileage_bargraph);
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 10));
        entries.add(new BarEntry(2, 8));
        entries.add(new BarEntry(3, 11));
        entries.add(new BarEntry(4, 12));
        BarDataSet dataSet = new BarDataSet(entries, "Distance");
        dataSet.setValueTextSize(10);
        dataSet.setColor(Color.parseColor("#b71c1c"));
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        //Refresh chart
        barChart.invalidate();

        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setGranularity(2);
        barChart.getAxisLeft().setSpaceTop(30);
        barChart.getAxisRight().setEnabled(false);

        barChart.getXAxis().setGranularity(1);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        String[] values = new String[]{"Jan", "Feb", "Mar", "Apr"};
        barChart.getXAxis().setValueFormatter(new AxisValueFormatter(values));

        barChart.animateXY(1500, 1500);
    }

}
