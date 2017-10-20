package com.example.tobias.run.stats;

import android.content.Context;

import com.example.tobias.run.R;
import com.example.tobias.run.utils.AxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Creates, adds dataset and styles graphs to be used in stats fragment.
 */

public class StatsGraphGenerator {

    private Context context;

    public StatsGraphGenerator(Context context){
        this.context = context;
    }

    /**
     * Creates bar chart to be used with mileage view pager.
     * @param dataSet  Data set for graph in list of entries
     * @param xAxisLabelValues X axis label values
     * @return Bar chart styled and with data set
     */
    public BarChart createMileageChart(List<BarEntry> dataSet, String[] xAxisLabelValues){
        BarChart graph = new BarChart(context);

        BarDataSet data = new BarDataSet(dataSet, "Distance");
        data.setValueTextSize(10);
        data.setColor(context.getResources().getColor(R.color.colorPrimary));

        graph.setData(new BarData(data));
        graph.invalidate();

        graph.getAxisLeft().setAxisMinimum(0);
        graph.getAxisLeft().setGranularity(2);
        graph.getAxisLeft().setSpaceTop(30);
        graph.getAxisRight().setEnabled(false);
        graph.getAxisRight().setDrawGridLines(false);
        graph.getAxisLeft().setDrawGridLines(true);

        graph.getXAxis().setGranularity(1);
        graph.getXAxis().setDrawGridLines(false);
        graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        graph.getXAxis().setValueFormatter(new AxisValueFormatter(xAxisLabelValues));

        graph.animateXY(1500, 1500);
        graph.setTouchEnabled(false);

        graph.setDrawGridBackground(false);

        graph.setDescription(null);

        return graph;
    }
}
