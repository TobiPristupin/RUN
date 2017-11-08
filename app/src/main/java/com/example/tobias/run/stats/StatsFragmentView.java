package com.example.tobias.run.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tobias.run.R;


/**
 * StatsFragmentView displays statistics of tracked runs with graphs
 */
public class StatsFragmentView extends Fragment {

    private View rootView;

    public StatsFragmentView(){
        //Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        return rootView;
    }


}