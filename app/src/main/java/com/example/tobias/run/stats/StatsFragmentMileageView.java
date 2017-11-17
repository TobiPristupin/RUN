package com.example.tobias.run.stats;

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
import com.example.tobias.run.data.SharedPreferenceManager;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.utils.AxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Tobi on 10/29/2017.
 */

public class StatsFragmentMileageView extends Fragment implements StatsMileageView {

    private View rootView;

    private BarChart barChartMonth;
    private BarChart barChart3Month;
    private BarChart barChart6Month;
    private BarChart barChartYear;
    private ViewAnimator viewAnimator;

    private StatsMileagePresenter presenter;
    private SharedPreferenceRepository sharedPrefRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_mileage, container, false);

        barChartMonth = styleBarChart(new BarChart(getContext()));
        barChart3Month = styleBarChart(new BarChart(getContext()));
        barChart6Month = styleBarChart(new BarChart(getContext()));
        barChartYear = styleBarChart(new BarChart(getContext()));

        initViewAnimator();
        initTabLayout();

        sharedPrefRepository = new SharedPreferenceManager(getContext());
        presenter = new StatsMileagePresenter(this, FirebaseDatabaseManager.getInstance(), sharedPrefRepository);

        return rootView;
    }

    private void initTabLayout(){
        TabLayout tabLayout = rootView.findViewById(R.id.stats_mileage_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Month"));
        tabLayout.addTab(tabLayout.newTab().setText("3Months"));
        tabLayout.addTab(tabLayout.newTab().setText("6Months"));
        tabLayout.addTab(tabLayout.newTab().setText("Year"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewAnimator.setDisplayedChild(tab.getPosition());
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
        viewAnimator = rootView.findViewById(R.id.stats_mileage_viewanimator);
        viewAnimator.addView(barChartMonth);
        viewAnimator.addView(barChart3Month);
        viewAnimator.addView(barChart6Month);
        viewAnimator.addView(barChartYear);
    }

    /**
     * Is passed an empty bar chart and returns same bar chart with styling and data set.
     * @param chart new empty chart
     * @return styled chart
     */
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

        chart.setNoDataText("Oh Dear! It's empty! Start by adding some runs.");
        chart.setNoDataTextColor(getResources().getColor(android.R.color.black));
        chart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);

        chart.setDescription(null);

        return chart;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    @Override
    public void setGraphMonthData(Map<Integer, Float> data) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(mapToBarEntry(data), "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChartMonth.setData(new BarData(barDataSet));
        barChartMonth.invalidate();
    }

    @Override
    public void setGraph3MonthData(Map<Integer, Float> data) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(mapToBarEntry(data), "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChart3Month.setData(new BarData(barDataSet));
        barChart3Month.invalidate();
    }

    @Override
    public void setGraph6MonthData(Map<Integer, Float> data) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(mapToBarEntry(data), "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChart6Month.setData(new BarData(barDataSet));
        barChart6Month.invalidate();
    }

    @Override
    public void setGraphYearData(Map<Integer, Float> data) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(mapToBarEntry(data), "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChartYear.setData(new BarData(barDataSet));
        barChartYear.invalidate();
    }

    private List<BarEntry> mapToBarEntry(Map<Integer, Float> data){
        List<BarEntry> barEntries = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : data.entrySet()){
            barEntries.add(new BarEntry(entry.getKey(), entry.getValue()));
        }

        return barEntries;
    }

    @Override
    public void setGraphMonthXLabel(String[] values) {
        barChartMonth.getXAxis().setValueFormatter(new AxisValueFormatter(values));
    }

    @Override
    public void setGraph3MonthXLabel(String[] values) {
        barChart3Month.getXAxis().setValueFormatter(new AxisValueFormatter(values));
    }

    @Override
    public void setGraph6MonthXLabel(String[] values) {
        barChart6Month.getXAxis().setValueFormatter(new AxisValueFormatter(values));
    }

    @Override
    public void setGraphYearXLabel(String[] values) {
        barChartYear.getXAxis().setValueFormatter(new AxisValueFormatter(values));
    }

}
