package com.example.tobias.run.stats.activities;

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
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.utils.TimeAxisValueFormatter;
import com.example.tobias.run.utils.TimeValueFormatter;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.List;

/**
 * Created by Tobi on 1/12/2018.
 */

public class StatsFragmentActivitiesView extends Fragment implements StatsActivitiesView {

    private View rootView;
    private StatsActivitiesPresenter presenter;

    private CombinedChart chartMonth;
    private CombinedChart chart3Months;
    private CombinedChart chart6Months;
    private CombinedChart chartYear;
    private ViewAnimator viewAnimator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_activities, container, false);

        chartMonth = styleChart(new CombinedChart(getContext()));
        chart3Months = styleChart(new CombinedChart(getContext()));
        chart6Months = styleChart(new CombinedChart(getContext()));
        chartYear = styleChart(new CombinedChart(getContext()));

        initViewAnimator();
        initTabLayout();

        presenter = new StatsActivitiesPresenter(this, FirebaseDatabaseManager.getInstance());

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    private void initTabLayout(){
        TabLayout tabLayout = rootView.findViewById(R.id.stats_activities_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("Month"));
        tabLayout.addTab(tabLayout.newTab().setText("3Months"));
        tabLayout.addTab(tabLayout.newTab().setText("6Months"));
        tabLayout.addTab(tabLayout.newTab().setText("Year"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewAnimator.setDisplayedChild(tab.getPosition());
                ((CombinedChart) viewAnimator.getCurrentView()).animateXY(1000, 1000);
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
        viewAnimator = rootView.findViewById(R.id.stats_activities_viewanimator);

        viewAnimator.addView(chartMonth);
        viewAnimator.addView(chart3Months);
        viewAnimator.addView(chart6Months);
        viewAnimator.addView(chartYear);
        viewAnimator.setInAnimation(getContext(), R.anim.fade_in);
        viewAnimator.setOutAnimation(getContext(), R.anim.fade_out);
    }

    private CombinedChart styleChart(CombinedChart chart){
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setGranularity(1);
        chart.getAxisLeft().setSpaceTop(60);
        chart.getAxisLeft().setValueFormatter(new TimeAxisValueFormatter());

        chart.getAxisLeft().setDrawAxisLine(true);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawLabels(false);

        chart.animateXY(1500, 1500);
        chart.setTouchEnabled(false);

        chart.setDrawGridBackground(false);

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.SCATTER, CombinedChart.DrawOrder.LINE});

        chart.setNoDataText("Oh Dear! It's empty! Start by adding some runs.");
        chart.setNoDataTextColor(getResources().getColor(android.R.color.black));
        chart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);

        chart.setDescription(null);

        return chart;
    }

    @Override
    public void setMonthChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData) {
        setChartData(scatterData, lineData, chartMonth);
    }

    @Override
    public void set3MonthsChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData) {
        setChartData(scatterData, lineData, chart3Months);
    }

    @Override
    public void set6MonthsChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData) {
        setChartData(scatterData, lineData, chart6Months);
    }

    @Override
    public void setYearChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData) {
        setChartData(scatterData, lineData, chartYear);
    }

    private void setChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineData, CombinedChart chart){
        if (scatterData == null){
            //No need to check other lists, because if scatterData is null remaining lists will be null.
            return;
        }

        CombinedData combinedData = new CombinedData();

        ScatterData scData = new ScatterData();
        ScatterDataSet scatterDataSet = new ScatterDataSet(scatterData, "Runs");
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterDataSet.setScatterShapeSize(25);
        scatterDataSet.setValueFormatter(new TimeValueFormatter());
        scatterDataSet.setDrawValues(true);
        scatterDataSet.setColor(getResources().getColor(R.color.DarkOrange));
        scData.addDataSet(scatterDataSet);

        LineData lnData = new LineData();
        LineDataSet lineDataSet = new LineDataSet(lineData, "");
        lineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawValues(false);

        lnData.addDataSet(lineDataSet);

        combinedData.setData(lnData);
        combinedData.setData(scData);
        chart.setData(combinedData);
    }
}
