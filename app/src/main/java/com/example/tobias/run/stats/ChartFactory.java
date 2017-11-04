package com.example.tobias.run.stats;

import android.content.Context;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;

/**
 * Created by Tobi on 10/30/2017.
 */

public class ChartFactory {

        public static Chart getChart(Context context, ChartsEnum chart){

            switch (chart){
                case MILEAGE_BAR_CHART :
                    return createMileageBarChart(context);

                default :
                    return null;
            }
        }

        private static BarChart createMileageBarChart(Context context){
            BarChart barChart = new BarChart(context);

            barChart.getAxisLeft().setAxisMinimum(0);
            barChart.getAxisLeft().setGranularity(1);
            barChart.getAxisLeft().setSpaceTop(30);
            barChart.getAxisRight().setEnabled(false);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.getAxisLeft().setDrawGridLines(false);

            barChart.getXAxis().setGranularity(1);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

            barChart.animateXY(1500, 1500);
            barChart.setTouchEnabled(false);

            barChart.setDrawGridBackground(false);

            barChart.setNoDataText("Oh Dear! It's empty! Start by adding some runs.");
            barChart.setNoDataTextColor(context.getResources().getColor(android.R.color.black));
            barChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);

            barChart.setDescription(null);

            return barChart;
        }
}
