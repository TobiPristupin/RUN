package com.example.tobias.run;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Stats fragment that shows personal records
 */

public class StatsPRsFragment extends Fragment{

    private View rootView;

    public StatsPRsFragment(){
        //Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_prs, container, false);
        return rootView;
    }
}
