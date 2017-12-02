package com.example.tobias.run.stats.personalrecords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.example.tobias.run.R;

/**
 * Created by Tobi on 11/28/2017.
 */

public class StatsFragmentPrsView extends Fragment implements StatsPrsView {

    private View rootView;
    private ViewAnimator viewAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_pr, container, false);


        initTabLayout();
        return rootView;
    }

    private void initTabLayout(){
        TabLayout tabLayout = rootView.findViewById(R.id.stats_prs_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("400m"));
        tabLayout.addTab(tabLayout.newTab().setText("Mile"));
        tabLayout.addTab(tabLayout.newTab().setText("5k"));
        tabLayout.addTab(tabLayout.newTab().setText("10k"));
        tabLayout.addTab(tabLayout.newTab().setText("21k"));
        tabLayout.addTab(tabLayout.newTab().setText("42k"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewAnimator.setDisplayedChild(tab.getPosition());

                switch (tab.getPosition()){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViewAnimator(){
        viewAnimator = rootView.findViewById(R.id.stats_prs_viewanimator);

    }
}
