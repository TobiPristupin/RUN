package com.example.tobias.run;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Tobi on 2/13/2017.
 */

public class LineGraphFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_line_graph, container, false);

        initGraph();

        return rootView;
    }

    private void initGraph(){
        GraphView graphView = (GraphView) rootView.findViewById(R.id.line_graph);
        LineGraphSeries<DataPoint> values = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(0, 2),
                new DataPoint(2, 5)

        });
        //Thickness of line
        values.setThickness(10);
        values.setDrawBackground(true);
        values.setBackgroundColor(R.color.Grey);
        values.setColor(Color.WHITE);


        graphView.addSeries(values);
        graphView.setTitle("Test Graph");
        graphView.setTitleColor(Color.WHITE);
        graphView.setTitleTextSize(55);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
    }
}
