package com.example.tobias.run;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

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
        LineChart lineChart = new LineChart(getContext());
        lineChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(lineChart);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 3));
        entries.add(new Entry(2, 5));
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
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
