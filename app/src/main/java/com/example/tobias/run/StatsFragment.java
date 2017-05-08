package com.example.tobias.run;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelcan.inkpageindicator.InkPageIndicator;

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
        initViewPager();
        return rootView;
    }

    private void initViewPager(){
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.stats_viewpager);
        viewPager.setAdapter(new StatsFragmentPagerAdapter(getChildFragmentManager()));
        InkPageIndicator pageIndicator = (InkPageIndicator) rootView.findViewById(R.id.stats_viewpager_indicator);
        pageIndicator.setViewPager(viewPager);
    }


}
