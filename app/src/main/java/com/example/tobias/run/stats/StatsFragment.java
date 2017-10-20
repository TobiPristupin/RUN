package com.example.tobias.run.stats;


import android.content.Context;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * StatsFragment displays statistics of tracked runs with graphs
 */
public class StatsFragment extends Fragment {

    private View rootView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");

    public StatsFragment(){
        //Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        initMileageTabLayout();
        return rootView;
    }

    private void initMileageTabLayout(){
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.stats_mileage_tablayout);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.stats_mileage_viewpager);
        viewPager.setAdapter(new MileagePagerAdapter(getContext()));
        tabLayout.setupWithViewPager(viewPager);
    }


    private class MileagePagerAdapter extends PagerAdapter {

        private String[] tabTitles = new String[]{"Month", "3Months", "6Months", "Year"};
        private int tabCount = 4;
        private Context context;

        MileagePagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BarChart barChart;
            List<BarEntry> dataSet;
            StatsGraphGenerator graphGenerator = new StatsGraphGenerator(getContext());

            switch (position){
                case 0 :
                    dataSet = new ArrayList<>();
                    dataSet.add(new BarEntry(1, 2));
                    dataSet.add(new BarEntry(2, 6));
                    barChart = graphGenerator.createMileageChart(dataSet, new String[]{"Jun", "Apr"});
                    break;

                default :
                    dataSet = new ArrayList<>();
                    dataSet.add(new BarEntry(1, 8));
                    dataSet.add(new BarEntry(2, 3));
                    barChart = graphGenerator.createMileageChart(dataSet, new String[]{"ba", "bi"});
            }
            container.addView(barChart);
            return barChart;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
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
