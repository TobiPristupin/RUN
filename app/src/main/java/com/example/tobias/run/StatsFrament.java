package com.example.tobias.run;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * StatsFragment is displayed when selecting {@code Stats} tab. Displays statistics of the running activities
 * tracked as well as graphs to support them.
 */
public class StatsFrament extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }


}
