package com.example.tobias.run;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by tobi on 3/7/17.
 */

public class GraphFragment extends Fragment {

    private View rootView;
    private LinearLayout layout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.stats_container, container, false);
        layout = (LinearLayout) rootView.findViewById(R.id.stats_container_layout);
        initGraph();
        return rootView;
    }

    public static GraphFragment newInstance(GraphVariants graphType){
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putSerializable("graphType", graphType);
        fragment.setArguments(args);
        return fragment;
    }

    private void initGraph(){
        Bundle args = getArguments();
        GraphVariants graphType = (GraphVariants) args.getSerializable("graphType");
        switch (graphType){
            case  BAR_GRAPH_MILEAGE_MONTH :
                initBarMileageMonth();
                break;
            case  BAR_GRAPH_MILEAGE_TRIMESTER:
                initBarMileageTrimester();
                break;
            case  BAR_GRAPH_MILEAGE_HALFYEAR:
                initBarMileageHalfYear();
                break;
            case  BAR_GRAPH_MILEAGE_YEAR:
                initBarMileageYear();
                break;
        }
    }

    private void initBarMileageMonth(){
        layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        GraphView graphView = new GraphView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        graphView.setLayoutParams(layoutParams);
        graphView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


        BarGraphSeries<DataPoint> dataSeries = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 3),
                new DataPoint(2, 7),
                new DataPoint(3, 2)
        });

        dataSeries.setTitle("test");
        dataSeries.setColor(Color.WHITE);
        dataSeries.setSpacing(10);
        dataSeries.setDrawValuesOnTop(true);
        dataSeries.setValuesOnTopColor(Color.WHITE);
        dataSeries.setAnimated(true);

        graphView.addSeries(dataSeries);

        layout.addView(graphView);
    }

    private void initBarMileageTrimester(){
        layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void initBarMileageHalfYear(){
        layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void initBarMileageYear(){
        layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }


}
