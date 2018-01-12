package com.example.tobias.run.stats.personalrecords;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.example.tobias.run.R;
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.SharedPreferenceManager;
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
 * Created by Tobi on 11/28/2017.
 */

public class StatsFragmentPrsView extends Fragment implements StatsPrsView {

    private View rootView;
    private ViewAnimator viewAnimator;

    private StatsPrsPresenter presenter;

    private CombinedChart chart400m;
    private CombinedChart chartMile;
    private CombinedChart chart5k;
    private CombinedChart chart10k;
    private CombinedChart chart21k;
    private CombinedChart chart42k;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_pr, container, false);

        chart400m = styleChart(new CombinedChart(getContext()));
        chartMile = styleChart(new CombinedChart(getContext()));
        chart5k = styleChart(new CombinedChart(getContext()));
        chart10k = styleChart(new CombinedChart(getContext()));
        chart21k = styleChart(new CombinedChart(getContext()));
        chart42k = styleChart(new CombinedChart(getContext()));

        initViewAnimator();
        initTabLayout();
        initPersonalBests();

        presenter = new StatsPrsPresenter(this, FirebaseDatabaseManager.getInstance(),
                new SharedPreferenceManager(getContext()));
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    private void initTabLayout(){
        TabLayout tabLayout = rootView.findViewById(R.id.stats_prs_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("400m"));
        tabLayout.addTab(tabLayout.newTab().setText("Mile"));
        tabLayout.addTab(tabLayout.newTab().setText("5k"));
        tabLayout.addTab(tabLayout.newTab().setText("10k"));
        tabLayout.addTab(tabLayout.newTab().setText("21k"));
        tabLayout.addTab(tabLayout.newTab().setText("42k"));

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
        viewAnimator = rootView.findViewById(R.id.stats_prs_viewanimator);
        viewAnimator.addView(chart400m);
        viewAnimator.addView(chartMile);
        viewAnimator.addView(chart5k);
        viewAnimator.addView(chart10k);
        viewAnimator.addView(chart21k);
        viewAnimator.addView(chart42k);
        viewAnimator.setInAnimation(getContext(), R.anim.fade_in);
        viewAnimator.setOutAnimation(getContext(), R.anim.fade_out);
    }
    
    private void initPersonalBests(){
        View farthestDistance = rootView.findViewById(R.id.stats_prs_farthest_run);
        ((TextView) farthestDistance.findViewById(R.id.personal_best_item_title)).setText("Farthest Distance");
        ((TextView) farthestDistance.findViewById(R.id.personal_best_item_date)).setText("?");
        ((TextView) farthestDistance.findViewById(R.id.personal_best_item_value)).setText("0");

        View fastestPace = rootView.findViewById(R.id.stats_prs_fastest_pace);
        ((TextView) fastestPace.findViewById(R.id.personal_best_item_title)).setText("Fastest Avg Pace");
        ((TextView) fastestPace.findViewById(R.id.personal_best_item_date)).setText("?");
        ((TextView) fastestPace.findViewById(R.id.personal_best_item_value)).setText("0");

        View longestDuration = rootView.findViewById(R.id.stats_prs_longest_duration);
        ((TextView) longestDuration.findViewById(R.id.personal_best_item_title)).setText("Longest Duration");
        ((TextView) longestDuration.findViewById(R.id.personal_best_item_date)).setText("?");
        ((TextView) longestDuration.findViewById(R.id.personal_best_item_value)).setText("0");
    }

    /**
     * Is passed an empty bar chart and returns same bar chart with styling and data set.
     * @param chart new empty chart
     * @return styled chart
     */
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
    public void set400mChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineDataBest, @Nullable List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart400m);
    }

    @Override
    public void setMileChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineDataBest, @Nullable List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chartMile);
    }

    @Override
    public void set5kChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineDataBest, @Nullable List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart5k);
    }

    @Override
    public void set10kChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineDataBest, @Nullable List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart10k);
    }

    @Override
    public void set21kChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineDataBest, @Nullable List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart21k);
    }

    @Override
    public void set42kChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineDataBest, @Nullable List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart42k);
    }

    @Override
    public void setFarthestDistanceText(String distance, String date) {
        ((TextView) rootView.findViewById(R.id.stats_prs_farthest_run).findViewById(R.id.personal_best_item_value))
                .setText(distance);

        ((TextView) rootView.findViewById(R.id.stats_prs_farthest_run).findViewById(R.id.personal_best_item_date))
                .setText(date);
    }

    @Override
    public void setLongestDurationText(String duration, String date) {
        ((TextView) rootView.findViewById(R.id.stats_prs_longest_duration).findViewById(R.id.personal_best_item_value))
                .setText(duration);

        ((TextView) rootView.findViewById(R.id.stats_prs_longest_duration).findViewById(R.id.personal_best_item_date))
                .setText(date);
    }

    @Override
    public void setFastestPaceText(String pace, String date) {
        ((TextView) rootView.findViewById(R.id.stats_prs_fastest_pace).findViewById(R.id.personal_best_item_value))
                .setText(pace);

        ((TextView) rootView.findViewById(R.id.stats_prs_fastest_pace).findViewById(R.id.personal_best_item_date))
                .setText(date);
    }

    private void setChartData(@Nullable List<Entry> scatterData, @Nullable List<Entry> lineDataBest, @Nullable List<Entry> lineDataAverage, CombinedChart combinedChart){
        if (scatterData == null){
            //No need to check other lists, because if scatterData is null remaining lists will be null.
            return;
        }

        CombinedData combinedData = new CombinedData();

        ScatterData scData = new ScatterData();
        ScatterDataSet scatterDataSet = new ScatterDataSet(scatterData, "Time");
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterDataSet.setScatterShapeSize(25);
        scatterDataSet.setValueFormatter(new TimeValueFormatter());
        scatterDataSet.setDrawValues(true);
        scatterDataSet.setColor(getResources().getColor(R.color.Orange));
        scData.addDataSet(scatterDataSet);

        LineData lineData = new LineData();
        LineDataSet bestLineDataSet = new LineDataSet(lineDataBest, "Best Time");
        bestLineDataSet.enableDashedLine(25, 15, 0);
        bestLineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
        bestLineDataSet.setDrawCircles(false);
        bestLineDataSet.setLineWidth(2);
        bestLineDataSet.setDrawValues(false);


        LineDataSet averageLineDataSet = new LineDataSet(lineDataAverage, "Average Time");
        averageLineDataSet.enableDashedLine(15, 5, 0);
        averageLineDataSet.setColor(getResources().getColor(R.color.Blue));
        averageLineDataSet.setDrawCircles(false);
        averageLineDataSet.setLineWidth(2);
        averageLineDataSet.setDrawValues(false);

        lineData.addDataSet(averageLineDataSet);
        lineData.addDataSet(bestLineDataSet);

        combinedData.setData(lineData);
        combinedData.setData(scData);
        combinedChart.setData(combinedData);
    }
}
