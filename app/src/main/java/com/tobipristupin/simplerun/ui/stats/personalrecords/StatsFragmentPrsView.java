package com.tobipristupin.simplerun.ui.stats.personalrecords;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseFragment;
import com.tobipristupin.simplerun.data.manager.FirebaseRunsSingleton;
import com.tobipristupin.simplerun.data.manager.SharedPrefRepository;
import com.tobipristupin.simplerun.utils.TimeValueFormatter;

import org.w3c.dom.Text;

import java.util.List;


public class StatsFragmentPrsView extends BaseFragment implements StatsPrsView {

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

        presenter = new StatsPrsPresenter(this, FirebaseRunsSingleton.getInstance(), new SharedPrefRepository(getContext()));
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    private void initTabLayout(){
        TabLayout tabLayout = rootView.findViewById(R.id.stats_prs_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.stats_fragment_prs_view_400m));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.stats_fragment_prs_view_mile));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.stats_fragment_prs_view_5k));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.stats_fragment_prs_view_10k));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.stats_fragment_prs_view_21k));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.stats_fragment_prs_view_42k));

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
        ((TextView) farthestDistance.findViewById(R.id.personal_best_item_title)).setText(R.string.stats_fragment_prs_view_farthest);

        View fastestPace = rootView.findViewById(R.id.stats_prs_fastest_pace);
        ((TextView) fastestPace.findViewById(R.id.personal_best_item_title)).setText(R.string.stats_fragment_prs_view_fastest);

        View longestDuration = rootView.findViewById(R.id.stats_prs_longest_duration);
        ((TextView) longestDuration.findViewById(R.id.personal_best_item_title)).setText(R.string.stats_fragment_prs_view_longest);
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
        chart.getAxisLeft().setValueFormatter(new TimeValueFormatter());

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

        chart.setNoDataText(getString(R.string.all_empty_dataset));
        chart.setNoDataTextColor(getResources().getColor(android.R.color.black));
        chart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);

        chart.setDescription(null);

        return chart;
    }

    @Override
    public void setGraph400mData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart400m);
    }

    @Override
    public void setGraphMileData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chartMile);
    }

    @Override
    public void setGraph5kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart5k);
    }

    @Override
    public void setGraph10kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart10k);
    }

    @Override
    public void setGraph21kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart21k);
    }

    @Override
    public void setGraph42kData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage) {
        setChartData(scatterData, lineDataBest, lineDataAverage, chart42k);
    }

    @Override
    public void setFarthestDistanceText(String distance, String date) {
        View personalBestItem = rootView.findViewById(R.id.stats_prs_farthest_run);
        TextView valueTextView = personalBestItem.findViewById(R.id.personal_best_item_value);
        TextView dateTextView = personalBestItem.findViewById(R.id.personal_best_item_date);

        setRecordsValue(valueTextView, distance);
        setRecordsDate(dateTextView, date);
    }

    @Override
    public void setLongestDurationText(String duration, String date) {
        View personalBestItem = rootView.findViewById(R.id.stats_prs_longest_duration);
        TextView valueTextView = personalBestItem.findViewById(R.id.personal_best_item_value);
        TextView dateTextView = personalBestItem.findViewById(R.id.personal_best_item_date);

        setRecordsValue(valueTextView, duration);
        setRecordsDate(dateTextView, date);
    }

    @Override
    public void setFastestPaceText(String pace, String date) {
        View personalBestItem = rootView.findViewById(R.id.stats_prs_fastest_pace);
        TextView valueTextView = personalBestItem.findViewById(R.id.personal_best_item_value);
        TextView dateTextView = personalBestItem.findViewById(R.id.personal_best_item_date);

        setRecordsValue(valueTextView, pace);
        setRecordsDate(dateTextView, date);
    }

    private void setRecordsValue(TextView view, String value){
        view.setText(value);
//        if (Integer.parseInt(value) > 0){
//            view.setTextColor(getResources().getColor(R.color.Green));
//        } else {
//            view.setTextColor(getResources().getColor(R.color.Red));
//        }

        view.setTextColor(getResources().getColor(R.color.Green));
    }

    private void setRecordsDate(TextView view, String value){
        view.setText(value);
    }

    private void setChartData(List<Entry> scatterData, List<Entry> lineDataBest, List<Entry> lineDataAverage, CombinedChart combinedChart){
        if (scatterData.isEmpty()){
            //No need to check other lists, because if scatterData is empty remaining lists will be empty.
            return;
        }

        CombinedData combinedData = new CombinedData();

        ScatterData scData = new ScatterData();
        ScatterDataSet scatterDataSet = new ScatterDataSet(scatterData, getString(R.string.stats_fragment_prs_view_time));
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterDataSet.setScatterShapeSize(25);
        scatterDataSet.setValueFormatter(new TimeValueFormatter());
        scatterDataSet.setDrawValues(true);
        scatterDataSet.setValueTextSize(11);
        scatterDataSet.setColor(getResources().getColor(R.color.Orange));
        scData.addDataSet(scatterDataSet);

        LineData lineData = new LineData();
        LineDataSet bestLineDataSet = new LineDataSet(lineDataBest, getString(R.string.stats_fragment_prs_view_besttime));
        bestLineDataSet.enableDashedLine(25, 15, 0);
        bestLineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
        bestLineDataSet.setDrawCircles(false);
        bestLineDataSet.setLineWidth(2);
        bestLineDataSet.setDrawValues(false);


        LineDataSet averageLineDataSet = new LineDataSet(lineDataAverage, getString(R.string.stats_fragment_prs_view_average));
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
