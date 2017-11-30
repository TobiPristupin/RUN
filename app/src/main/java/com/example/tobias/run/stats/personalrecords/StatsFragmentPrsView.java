package com.example.tobias.run.stats.personalrecords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tobias.run.R;

/**
 * Created by Tobi on 11/28/2017.
 */

public class StatsFragmentPrsView extends Fragment implements StatsPrsView {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_pr, container, false);
        return rootView;
    }
}
