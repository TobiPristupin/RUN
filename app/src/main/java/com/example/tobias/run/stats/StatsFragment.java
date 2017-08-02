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
import com.example.tobias.run.stats.fragment.StatsMileageGraphFragment;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

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
        initTabLayout();
        return rootView;
    }

    private void initTabLayout(){
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.stats_mileage_tablayout);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.stats_mileage_viewpager);
        viewPager.setAdapter(new MileagePagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }


    public class MileagePagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"Month", "3Months", "6Months", "Year"};
        private int tabCount = 4;

        public MileagePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            switch (position){
//                case 0 :
//                    return StatsMileageGraphFragment.newInstance(entries);
//            }

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
            entries.add(new BarEntry(1, 5));
            return StatsMileageGraphFragment.newInstance(entries);
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
