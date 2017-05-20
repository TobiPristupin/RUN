package com.example.tobias.run.statsscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tobias.run.R;
import com.example.tobias.run.helpers.AxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Stats fragment that displays graph to ViewPager for StatsFragment.
 */

public class StatsMileageGraphFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_graph_layout, container, false);
        initBarchart();
        return rootView;
    }

    public static StatsMileageGraphFragment newInstance(ArrayList<BarEntry> chartData) {
        StatsMileageGraphFragment fragment = new StatsMileageGraphFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("chartData", chartData);
        fragment.setArguments(args);
        return fragment;
    }


    private void initBarchart() {
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.stats_graph_layout);
        BarChart barChart = new BarChart(getContext());
        barChart.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        linearLayout.addView(barChart);


        List<BarEntry> chartData = getChartData();
        BarDataSet dataSet = new BarDataSet(chartData, "Distance");
        dataSet.setValueTextSize(10);
        dataSet.setColor(Color.parseColor("#b71c1c"));
        BarData barData = new BarData(dataSet);


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

        barChart.setData(barData);
        barChart.invalidate();

    }

    private ArrayList<BarEntry> getChartData(){
        Bundle args = getArguments();
        return args.getParcelableArrayList("chartData");
    }

}
