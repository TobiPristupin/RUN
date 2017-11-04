package com.example.tobias.run.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tobias.run.R;
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.utils.AxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
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
    private StatsMileagePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_mileage, container, false);

        barChartMonth = (BarChart) ChartFactory.getChart(getContext(), ChartsEnum.MILEAGE_BAR_CHART);
        barChart3Month = (BarChart) ChartFactory.getChart(getContext(), ChartsEnum.MILEAGE_BAR_CHART);
        barChart6Month = (BarChart) ChartFactory.getChart(getContext(), ChartsEnum.MILEAGE_BAR_CHART);
        barChartYear = (BarChart) ChartFactory.getChart(getContext(), ChartsEnum.MILEAGE_BAR_CHART);

        presenter = new StatsMileagePresenter(this, FirebaseDatabaseManager.getInstance());
        
        initTabLayout();
        
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    private void initTabLayout(){
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.stats_mileage_tablayout);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.stats_mileage_viewpager);
        viewPager.setAdapter(new MileagePagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void setGraphMonthData(List<BarEntry> data) {
        BarDataSet barDataSet = new BarDataSet(data, "Distance");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChartMonth.setData(new BarData(barDataSet));
        barChartMonth.invalidate();
    }

    @Override
    public void setGraph3MonthData(List<BarEntry> data) {
        BarDataSet barDataSet = new BarDataSet(data, "Distance");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChart3Month.setData(new BarData(barDataSet));
        barChart3Month.invalidate();
    }

    @Override
    public void setGraph6MonthData(List<BarEntry> data) {
        BarDataSet barDataSet = new BarDataSet(data, "Distance");
        barDataSet.setValueTextSize(10);
        barDataSet.setColor(getResources().getColor(R.color.DarkPink));

        barChart6Month.setData(new BarData(barDataSet));
        barChart6Month.invalidate();
    }

    @Override
    public void setGraphYearData(List<BarEntry> data) {
        BarDataSet barDataSet = new BarDataSet(data, "Distance");
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

    }

    @Override
    public void setGraphYearXLabel(String[] values) {

    }

    private class MileagePagerAdapter extends PagerAdapter {

        private String[] tabTitles = new String[]{"Month", "3Months", "6Months", "Year"};
        private int tabCount = 4;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
           switch (position){
               case 0 :
                   container.addView(barChartMonth);
                   return barChartMonth;
               case 1 :
                   container.addView(barChart3Month);
                   return barChart3Month;
               case 2 :
                   container.addView(barChart6Month);
                   return barChart6Month;
               case 3 :
                   container.addView(barChartYear);
                   return barChartYear;
           }

           return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((BarChart) object);
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
