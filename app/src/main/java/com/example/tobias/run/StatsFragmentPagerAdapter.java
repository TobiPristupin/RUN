package com.example.tobias.run;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Pager adapter for fragments in stats
 */

public class StatsFragmentPagerAdapter extends FragmentPagerAdapter{

    private int pageCount = 2;

    public StatsFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new StatsMileageFragment();
            case 1 :
                return new StatsPRsFragment();

        }

        return null;
    }
}
