package com.example.tobias.run;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * StatsFragment displays statistics of tracked runs with graphs. Can be accessed via DrawerLayout in MainActivity
 */
public class StatsFragment extends Fragment {

    private View rootView;

    public StatsFragment(){
        //Required empty constructor.
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        initButtons();
        return rootView;
    }

    private void initButtons(){
        ((RelativeLayout)  rootView.findViewById(R.id.stats_mileage_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StatsMileageActivity.class);
                startActivity(intent);
            }
        });

        ((RelativeLayout) rootView.findViewById(R.id.stats_workouts_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Tracked Workouts");
            }
        });

        ((RelativeLayout) rootView.findViewById(R.id.stats_records_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Records");
            }
        });

        ((RelativeLayout) rootView.findViewById(R.id.stats_misc_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Misc");
            }
        });
    }


}
