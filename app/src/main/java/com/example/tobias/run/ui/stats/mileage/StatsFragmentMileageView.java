package com.example.tobias.run.ui.stats.mileage;

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
import com.example.tobias.run.data.interfaces.Repository;
import com.example.tobias.run.data.manager.FirebaseRunsSingleton;
import com.example.tobias.run.data.manager.FirebaseRepository;
import com.example.tobias.run.data.manager.FirebaseSettingsSingleton;
import com.example.tobias.run.data.model.Distance;
import com.example.tobias.run.utils.GenericAxisValueFormatter;
import com.example.tobias.run.utils.StateChange;
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

    private BarChart barChartWeek;
    private BarChart barChartMonth;
    private BarChart barChart3Month;
    private BarChart barChart6Month;
    private BarChart barChartYear;

    private String weekTotalDistance;
    private String monthTotalDistance;
    private String month3TotalDistance;
    private String month6TotalDistance;
    private String yearTotalDistance;

    private ViewAnimator viewAnimator;
    private TextView totalDistanceTextView;
    private TabLayout tabLayout;

    private StatsMileagePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_mileage, container, false);


        barChartWeek = styleBarChart(new BarChart(getContext()));
        barChartMonth = styleBarChart(new BarChart(getContext()));
        barChart3Month = styleBarChart(new BarChart(getContext()));
        barChart6Month = styleBarChart(new BarChart(getContext()));
        barChartYear = styleBarChart(new BarChart(getContext()));
        totalDistanceTextView = rootView.findViewById(R.id.stats_mileage_totaldistance_text);

        initOverviewItems();
        initViewAnimator();
        initTabLayout();


        presenter = new StatsMileagePresenter(this, FirebaseRunsSingleton.getInstance());


        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    private void initTabLayout(){
        tabLayout = rootView.findViewById(R.id.stats_mileage_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Week"));
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
                        totalDistanceTextView.setText(weekTotalDistance);
                        break;
                    case 1 :
                        totalDistanceTextView.setText(monthTotalDistance);
                        break;
                    case 2 :
                        totalDistanceTextView.setText(month3TotalDistance);
                        break;
                    case 3 :
                        totalDistanceTextView.setText(month6TotalDistance);
                        break;
                    case 4 :
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
        Distance.Unit unit = FirebaseSettingsSingleton.getInstance().getDistanceUnit();

        View overviewItemMonth = rootView.findViewById(R.id.stats_mileage_overview_item_month);
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_value_text)).setText("This month");
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_increase_text)).setText("Over last month");
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_value)).setText(0 + unit.toString());
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItemMonth.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));

        View overviewItem3Months = rootView.findViewById(R.id.stats_mileage_overview_item_3months);
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_value_text)).setText("This 3 months");
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_increase_text)).setText("Over last 3 months");
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_value)).setText(0 + unit.toString());
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItem3Months.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));

        View overviewItem6Months = rootView.findViewById(R.id.stats_mileage_overview_item_6months);
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_value_text)).setText("This 6 months");
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_increase_text)).setText("Over last 6 months");
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_value)).setText(0 + unit.toString());
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItem6Months.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));

        View overviewItemYear = rootView.findViewById(R.id.stats_mileage_overview_item_year);
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_value_text)).setText("This year");
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_increase_text)).setText("Over last year");
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_value)).setText(0 + unit.toString());
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_increase_value)).setText(0 + "+");
        ((TextView) overviewItemYear.findViewById(R.id.overview_item_increase_value)).setTextColor(getResources().getColor(R.color.Green));
    }

    private void initViewAnimator(){
        viewAnimator = rootView.findViewById(R.id.stats_mileage_viewanimator);
        viewAnimator.addView(barChartWeek);
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
    public void setGraphWeekData(List<BarEntry> barEntries) {
        setGraphData(barEntries, barChartWeek);
    }

    @Override
    public void setGraphMonthData(List<BarEntry> barEntries) {
        setGraphData(barEntries, barChartMonth);
    }

    @Override
    public void setGraph3MonthData(List<BarEntry> barEntries) {
        setGraphData(barEntries, barChart3Month);
    }

    @Override
    public void setGraph6MonthData(List<BarEntry> barEntries) {
        setGraphData(barEntries, barChart6Month);
    }

    @Override
    public void setGraphYearData(List<BarEntry> barEntries) {
        setGraphData(barEntries, barChartYear);
    }
    
    @Override
    public void setGraphWeekXLabel(String[] values) {
        barChartWeek.getXAxis().setValueFormatter(new GenericAxisValueFormatter<>(values));
    }

    @Override
    public void setGraphMonthXLabel(String[] values) {
        barChartMonth.getXAxis().setValueFormatter(new GenericAxisValueFormatter<>(values));
    }

    @Override
    public void setGraph3MonthXLabel(String[] values) {
        barChart3Month.getXAxis().setValueFormatter(new GenericAxisValueFormatter<>(values));
    }

    @Override
    public void setGraph6MonthXLabel(String[] values) {
        barChart6Month.getXAxis().setValueFormatter(new GenericAxisValueFormatter<>(values));
    }

    @Override
    public void setGraphYearXLabel(String[] values) {
        barChartYear.getXAxis().setValueFormatter(new GenericAxisValueFormatter<>(values));
    }

    @Override
    public void setTotalDistanceWeek(String text){
        weekTotalDistance = text;
        /**
         * totalDistanceTextView text is updated when any tab is selected. Because presenter has a delay
         * when retrieving data from the network, when tab 0 is selected the totalDistanceMonth text has not
         * been initialized and so the textview's text is null. The following lines of code ensure that as soon
         * as the presenter retrieves the data, the textview is updated. This code is not in the rest of the setDistance...
         * methods because by the time the user switches to another tab, the presenter already updated
         * all the totalDistance strings.
         */
        if (tabLayout.getSelectedTabPosition() == 0){
            totalDistanceTextView.setText(weekTotalDistance);
        }
    }

    @Override
    public void setTotalDistanceMonth(String text) {
        ((TextView) rootView.findViewById(R.id.stats_mileage_overview_item_month).findViewById(R.id.overview_item_value))
                .setText(text);

        monthTotalDistance = text;
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
    public void setMonthIncreaseText(String text, StateChange change) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_month).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, change, textView);
    }

    @Override
    public void set3MonthsIncreaseText(String text, StateChange change) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_3months).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, change, textView);
    }

    @Override
    public void set6MonthsIncreaseText(String text, StateChange change) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_6months).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, change, textView);
    }

    @Override
    public void setYearIncreaseText(String text, StateChange change) {
        TextView textView = rootView.findViewById(R.id.stats_mileage_overview_item_year).findViewById(R.id.overview_item_increase_value);
        setIncreaseText(text, change, textView);
    }

    private void setGraphData(List<BarEntry> barEntries, BarChart chart){
        Distance.Unit unit = FirebaseSettingsSingleton.getInstance().getDistanceUnit();
        BarDataSet barDataSet = new BarDataSet(barEntries, "Distance (" + unit + ")");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        chart.setData(new BarData(barDataSet));
        chart.invalidate();
    }

    private void setIncreaseText(String text, StateChange change, TextView textView){
        textView.setText(text);
        if (change == StateChange.INCREASE){
            textView.setTextColor(getResources().getColor(R.color.Green));
        } else if (change == StateChange.DECREASE) {
            textView.setTextColor(getResources().getColor(R.color.Red));
        }
    }
}