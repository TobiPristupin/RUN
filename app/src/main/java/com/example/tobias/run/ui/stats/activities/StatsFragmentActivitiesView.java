package com.example.tobias.run.ui.stats.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.example.tobias.run.R;
import com.example.tobias.run.data.manager.FirebaseRunsSingleton;
import com.example.tobias.run.utils.GenericAxisValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 1/12/2018.
 */

public class StatsFragmentActivitiesView extends Fragment implements StatsActivitiesView {

    private View rootView;
    private StatsActivitiesPresenter presenter;

    private BarChart chartWeek;
    private BarChart chartMonth;
    private BarChart chart3Months;
    private BarChart chart6Months;
    private BarChart chartYear;
    private ViewAnimator viewAnimator;
    private PieChart pieChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_activities, container, false);

        pieChart = rootView.findViewById(R.id.stats_activities_distribution_chart);
        chartWeek = styleBarChart(new BarChart(getContext()));
        chartMonth = styleBarChart(new BarChart(getContext()));
        chart3Months = styleBarChart(new BarChart(getContext()));
        chart6Months = styleBarChart(new BarChart(getContext()));
        chartYear = styleBarChart(new BarChart(getContext()));



        initViewAnimator();
        initTabLayout();
        initPieChart();

        presenter = new StatsActivitiesPresenter(this, FirebaseRunsSingleton.getInstance());

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    private void initTabLayout(){
        TabLayout tabLayout = rootView.findViewById(R.id.stats_activities_frequency_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("Week"));
        tabLayout.addTab(tabLayout.newTab().setText("Month"));
        tabLayout.addTab(tabLayout.newTab().setText("3Months"));
        tabLayout.addTab(tabLayout.newTab().setText("6Months"));
        tabLayout.addTab(tabLayout.newTab().setText("Year"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewAnimator.setDisplayedChild(tab.getPosition());
                ((Chart) viewAnimator.getCurrentView()).animateXY(1000, 1000);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViewAnimator(){
        viewAnimator = rootView.findViewById(R.id.stats_activities_frequency_viewanimator);

        viewAnimator.addView(chartWeek);
        viewAnimator.addView(chartMonth);
        viewAnimator.addView(chart3Months);
        viewAnimator.addView(chart6Months);
        viewAnimator.addView(chartYear);
        viewAnimator.setInAnimation(getContext(), R.anim.fade_in);
        viewAnimator.setOutAnimation(getContext(), R.anim.fade_out);
    }

    private BarChart styleBarChart(BarChart chart){
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setGranularity(1);
        chart.getAxisLeft().setSpaceTop(30);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);

        chart.getXAxis().setGranularity(1);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.animateXY(1500, 1500);
        chart.setTouchEnabled(false);

        chart.setDrawGridBackground(false);

        chart.setNoDataText("Oh no! It's empty!");
        chart.setNoDataTextColor(getResources().getColor(android.R.color.black));
        chart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);

        chart.setDescription(null);

        return chart;
    }

    private void initPieChart(){
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.setEntryLabelTextSize(13);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.getLegend().setEnabled(false);
        pieChart.setNoDataText("Oh no! It's empty!");
        pieChart.setNoDataTextColor(getResources().getColor(android.R.color.black));
        pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public void setGraphWeekData(List<BarEntry> barData) {
        setChartData(barData, chartWeek);
    }

    @Override
    public void setGraphMonthData(List<BarEntry> barData) {
        setChartData(barData, chartMonth);
    }

    @Override
    public void setGraph3MonthsData(List<BarEntry> barData) {
        setChartData(barData, chart3Months);
    }

    @Override
    public void setGraph6MonthsData(List<BarEntry> barData) {
        setChartData(barData, chart6Months);
    }

    @Override
    public void setGraphYearData(List<BarEntry> barData) {
        setChartData(barData, chartYear);
    }

    @Override
    public void setGraphWeekXLabels(String[] labels) {
        chartWeek.getXAxis().setValueFormatter(new GenericAxisValueFormatter<String>(labels));
    }

    @Override
    public void setGraphMonthXLabels(String[] labels) {
        chartMonth.getXAxis().setValueFormatter(new GenericAxisValueFormatter<String>(labels));
    }

    @Override
    public void setGraph3MonthsXLabels(String[] labels) {
        chart3Months.getXAxis().setValueFormatter(new GenericAxisValueFormatter<String>(labels));
    }

    @Override
    public void setGraph6MonthsXLabels(String[] labels) {
        chart6Months.getXAxis().setValueFormatter(new GenericAxisValueFormatter<String>(labels));
    }

    @Override
    public void setGraphYearXLabels(String[] labels) {
        chartYear.getXAxis().setValueFormatter(new GenericAxisValueFormatter<String>(labels));
    }

    @Override
    public void setPieChartData(List<PieEntry> data) {
        PieDataSet set = new PieDataSet(data, "Distribution");
        set.setValueFormatter(new PercentFormatter());

        List<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.COLORFUL_COLORS){
            colors.add(c);
        }

        set.setColors(colors);

        PieData pieData = new PieData(set);
        pieData.setValueTextSize(11);
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieData.setValueTextColor(getResources().getColor(android.R.color.white));
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void setChartData(List<BarEntry> barData, BarChart chart){
        BarDataSet barDataSet = new BarDataSet(barData, "Activities");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkOrange));
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });

        chart.setData(new BarData(barDataSet));
        chart.invalidate();
    }
}
