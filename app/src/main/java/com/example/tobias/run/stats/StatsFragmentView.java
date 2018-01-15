package com.example.tobias.run.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tobias.run.R;
import com.example.tobias.run.stats.activities.StatsFragmentActivitiesView;
import com.example.tobias.run.stats.mileage.StatsFragmentMileageView;
import com.example.tobias.run.stats.personalrecords.StatsFragmentPrsView;


/**
 * StatsFragmentView displays statistics of tracked runs with graphs
 */
public class StatsFragmentView extends Fragment {

    private static final int TAB_COUNT = 3;
    private View rootView;

    public StatsFragmentView(){
        //Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        initViewPager();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Analyze");
    }

    private void initViewPager(){
        final TabLayout tabLayout = rootView.findViewById(R.id.stats_tab_layout);
        ViewPager viewPager = rootView.findViewById(R.id.stats_viewpager);
        viewPager.setAdapter(new StatsPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private class StatsPagerAdapter extends FragmentPagerAdapter {

        private String[] TAB_TITLES = {"Mileage", "PR's", "Activities"};

        public StatsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0 :
                    return new StatsFragmentMileageView();
                case 1 :
                    return new StatsFragmentPrsView();
                case 2 :
                    return new StatsFragmentActivitiesView();
                default :
                    throw new RuntimeException("Invalid tab position");
            }

        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }


}
