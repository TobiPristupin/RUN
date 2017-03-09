package com.example.tobias.run;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tobi on 3/6/17.
 */

public class StatsPagerAdapter extends FragmentPagerAdapter {


    public StatsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return GraphFragment.newInstance(GraphVariants.BAR_GRAPH_MILEAGE_TRIMESTER);
        }

        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Test";
    }
}
