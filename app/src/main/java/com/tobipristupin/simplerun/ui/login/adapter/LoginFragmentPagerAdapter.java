package com.tobipristupin.simplerun.ui.login.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tobipristupin.simplerun.ui.login.fragments.ForgotPasswordFragmentView;
import com.tobipristupin.simplerun.ui.login.fragments.LoginFragmentView;
import com.tobipristupin.simplerun.ui.login.fragments.NewAccountFragmentView;

/**
 * Created by Tobi on 9/15/2017.
 */

public class LoginFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final static int ITEMS = 3;

    public LoginFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new ForgotPasswordFragmentView();
            case 1 :
                return new LoginFragmentView();
            case 2 :
                return new NewAccountFragmentView();
            default :
                return null;
        }
    }
}
