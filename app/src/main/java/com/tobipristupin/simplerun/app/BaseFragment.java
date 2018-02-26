package com.tobipristupin.simplerun.app;

import android.support.v4.app.Fragment;

/**
 * Base fragment for all app that contains base methods
 */

public class BaseFragment extends Fragment {

    /**
     * Adds reference watcher from leak canary to detect memory leaks
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        RunApplication.getRefWatcher(getActivity()).watch(this);
    }
}
