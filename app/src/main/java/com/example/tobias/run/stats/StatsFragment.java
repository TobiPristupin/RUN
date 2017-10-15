package com.example.tobias.run.stats;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tobias.run.R;
import com.example.tobias.run.stats.fragment.MileageGraphFragment;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * StatsFragment displays statistics of tracked runs with graphs. Can be accessed via DrawerLayout in MainActivity
 */
public class StatsFragment extends Fragment {

    private View rootView;

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
        viewPager.setAdapter(new MileagePagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }


    private class MileagePagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"Month", "3Months", "6Months", "Year"};
        private int tabCount = 4;

        MileagePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    ArrayList<BarEntry> data = new ArrayList<>();
                    data.add(new BarEntry(1, 3));
                    return MileageGraphFragment.newInstance(getContext(), data, new String[] {"Jan"});
                case 1 :
                    ArrayList<BarEntry> data2 = new ArrayList<>();
                    data2.add(new BarEntry(1f, 20f));
                    data2.add(new BarEntry(2f, 3f));
                    data2.add(new BarEntry(3f, 5f));
                    return MileageGraphFragment.newInstance(getContext(), data2, new String[] {"Jan", "Feb", "Mar"});

                default :
                    ArrayList<BarEntry> data3 = new ArrayList<>();
                    data3.add(new BarEntry(1, 5));
                    data3.add(new BarEntry(2, 7));
                    data3.add(new BarEntry(3, 6));
                    return MileageGraphFragment.newInstance(getContext(), data3, new String[] {"Jan", "Mar", "June"});
            }
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
