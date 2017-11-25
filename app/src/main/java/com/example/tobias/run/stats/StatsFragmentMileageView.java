package com.example.tobias.run.stats;

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
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.utils.AxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;


/**
 * Created by Tobi on 10/29/2017.
 */

public class StatsFragmentMileageView extends Fragment implements StatsMileageView {

    private View rootView;

    private BarChart barChartMonth;
    private BarChart barChart3Month;
    private BarChart barChart6Month;
    private BarChart barChartYear;

    private String monthTotalDistance;
    private String month3TotalDistance;
    private String month6TotalDistance;
    private String yearTotalDistance;

    private ViewAnimator viewAnimator;
    private TextView totalDistanceTextView;
    private TabLayout tabLayout;

    private StatsMileagePresenter presenter;
    private SharedPreferenceRepository sharedPrefRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_mileage, container, false);

        sharedPrefRepository = new SharedPreferenceManager(getContext());

        barChartMonth = styleBarChart(new BarChart(getContext()));
        barChart3Month = styleBarChart(new BarChart(getContext()));
        barChart6Month = styleBarChart(new BarChart(getContext()));
        barChartYear = styleBarChart(new BarChart(getContext()));
        totalDistanceTextView = rootView.findViewById(R.id.stats_mileage_totaldistance_text);

        initOverviewItems();
        initViewAnimator();
        initTabLayout();


        presenter = new StatsMileagePresenter(this, FirebaseDatabaseManager.getInstance(), sharedPrefRepository);

        return rootView;
    }

    private void initTabLayout(){
        tabLayout = rootView.findViewById(R.id.stats_mileage_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Month"));
        tabLayout.addTab(tabLayout.newTab().setText("3Months"));
        tabLayout.addTab(tabLayout.newTab().setText("6Months"));
        tabLayout.addTab(tabLayout.newTab().setText("Year"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewAnimator.setDisplayedChild(tab.getPosition());
                ((BarChart) viewAnimator.getCurrentView()).animateXY(800, 800);

                switch (tab.getPosition()){
                    case 0 :
                        totalDistanceTextView.setText(monthTotalDistance);
                        break;
                    case 1 :
                        totalDistanceTextView.setText(month3TotalDistance);
                        break;
                    case 2 :
                        totalDistanceTextView.setText(month6TotalDistance);
                        break;
                    case 3 :
                        totalDistanceTextView.setText(yearTotalDistance);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initOverviewItems(){
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);

        View overviewItemMonth = rootView.findViewById(R.id.stats_mileage_overview_item_month);
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_value_text)).setText("This month");
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_increase_text)).setText("Over last month");
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_value)).setText(0 + unit);
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));

        View overviewItem3Months = rootView.findViewById(R.id.stats_mileage_overview_item_3months);
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_value_text)).setText("This 3 months");
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_increase_text)).setText("Over last 3 months");
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_value)).setText(0 + unit);
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));

        View overviewItem6Months = rootView.findViewById(R.id.stats_mileage_overview_item_6months);
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_value_text)).setText("This 6 months");
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_increase_text)).setText("Over last 6 months");
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_value)).setText(0 + unit);
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));

        View overviewItemYear = rootView.findViewById(R.id.stats_mileage_overview_item_year);
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_value_text)).setText("This year");
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_increase_text)).setText("Over last year");
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_value)).setText(0 + unit);
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));
    }

    private void initViewAnimator(){
        viewAnimator = rootView.findViewById(R.id.stats_mileage_viewanimator);
        viewAnimator.addView(barChartMonth);
        viewAnimator.addView(barChart3Month);
        viewAnimator.addView(barChart6Month);
        viewAnimator.addView(barChartYear);

        viewAnimator.setInAnimation(getContext(), R.anim.fade_in);
        viewAnimator.setOutAnimation(getContext(), R.anim.fade_out);
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
    public void setGraphMonthData(List<BarEntry> barEntries) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(barEntries, "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChartMonth.setData(new BarData(barDataSet));
        barChartMonth.invalidate();
    }

    @Override
    public void setGraph3MonthData(List<BarEntry> barEntries) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(barEntries, "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChart3Month.setData(new BarData(barDataSet));
        barChart3Month.invalidate();
    }

    @Override
    public void setGraph6MonthData(List<BarEntry> barEntries) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(barEntries, "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChart6Month.setData(new BarData(barDataSet));
        barChart6Month.invalidate();
    }

    @Override
    public void setGraphYearData(List<BarEntry> barEntries) {
        String unit = sharedPrefRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        BarDataSet barDataSet = new BarDataSet(barEntries, "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChartYear.setData(new BarData(barDataSet));
        barChartYear.invalidate();
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

    @Override
    public void setTotalDistanceMonth(String text) {
        ((TextView) rootView.findViewById(R.id.stats_mileage_overview_item_month).findViewById(R.id.overview_item_value))
                .setText(text);

        monthTotalDistance = text;
        /**
         * totalDistanceTextView text is updated when any tab is selected. Because presenter has a delay
         * when retrieving data from the network, when tab 0 is selected the totalDistanceMonth text has not
         * been initialized and so the textview's text is null. The following lines of code ensure that as soon
         * as the presenter retrieves the data, the textview is updated. This code is not in the rest of the setDistance...
         * methods because by the time the user switches to another tab, the presenter already updated
         * all the totalDistance strings.
         */
        if (tabLayout.getSelectedTabPosition() == 0){
            totalDistanceTextView.setText(monthTotalDistance);
        }
    }

    @Override
    public void setTotalDistance3Months(String text) {
        ((TextView) rootView.findViewById(R.id.stats_mileage_overview_item_3months).findViewById(R.id.overview_item_value))
                .setText(text);
        month3TotalDistance = text;
    }

    @Override
    public void setTotalDistance6Months(String text) {
        ((TextView) rootView.findViewById(R.id.stats_mileage_overview_item_6months).findViewById(R.id.overview_item_value))
                .setText(text);
        month6TotalDistance = text;
    }

    @Override
    public void setTotalDistanceYear(String text) {
        ((TextView) rootView.findViewById(R.id.stats_mileage_overview_item_year).findViewById(R.id.overview_item_value))
                .setText(text);
        yearTotalDistance = text;
    }

    @Override
    public void setMonthIncreaseText(String text, boolean isIncrease) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_month).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, isIncrease, textView);
    }

    @Override
    public void set3MonthsIncreaseText(String text, boolean isIncrease) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_3months).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, isIncrease, textView);
    }

    @Override
    public void set6MonthsIncreaseText(String text, boolean isIncrease) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_6months).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, isIncrease, textView);
    }

    @Override
    public void setYearIncreaseText(String text, boolean isIncrease) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_year).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, isIncrease, textView);
    }

    private void setIncreaseText(String text, boolean isIncrease, TextView textView){
        textView.setText(text);
        if (isIncrease){
            textView.setTextColor(getResources().getColor(R.color.Green));
        } else {
            textView.setTextColor(getResources().getColor(R.color.Red));
        }
    }
}
