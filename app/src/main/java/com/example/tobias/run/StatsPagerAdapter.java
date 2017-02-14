package com.example.tobias.run;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by Tobi on 2/13/2017.
 */

public class StatsPagerAdapter extends FragmentPagerAdapter {


    public StatsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new BarGraphFragment();
            case 1 :
                return new LineGraphFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
