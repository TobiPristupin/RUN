package com.tobipristupin.simplerun.app;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

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

    protected <T extends ViewModel> T obtainViewModel(Fragment fragment, Class<T> c){
        return ViewModelProviders.of(fragment).get(c);
    }
}
