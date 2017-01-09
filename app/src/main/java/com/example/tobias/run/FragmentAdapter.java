package com.example.tobias.run;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * FragmentAdapter extends from android's default FragmentPagerAdapter to switch between fragments in a ViewPager.
 * This class outputs to the UI the apropiate fragments and tab names of the Tablayout in MainActivity.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private int PAGE_COUNT = 2;
    private String[] PAGE_NAMES = new String[] {"History", "Stats"};

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new HistoryFragment();
        } else {
            return new StatsFrament();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_NAMES[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
