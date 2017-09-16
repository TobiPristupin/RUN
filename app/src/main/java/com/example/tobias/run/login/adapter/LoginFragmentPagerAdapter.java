package com.example.tobias.run.login.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tobias.run.login.ForgotPasswordFragment;
import com.example.tobias.run.login.LoginFragment;
import com.example.tobias.run.login.NewAccountFragment;

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
                return new ForgotPasswordFragment();
            case 1 :
                return new LoginFragment();
            case 2 :
                return new NewAccountFragment();
            default :
                return null;
        }
    }
}
