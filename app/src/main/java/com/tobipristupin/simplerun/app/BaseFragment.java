package com.tobipristupin.simplerun.app;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.tobipristupin.simplerun.R;

/**
 * Base fragment for all app that contains base methods
 */

public abstract class BaseFragment extends Fragment {

    /**
     * Adds reference watcher from leak canary to detect memory leaks
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        RunApplication.getRefWatcher(getActivity()).watch(this);
    }

    protected void changeStatusBarTitle(int resId){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(resId);
    }
}
