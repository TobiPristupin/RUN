package com.example.tobias.run.stats.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tobias.run.R;
import com.example.tobias.run.utils.AxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MileageGraphFragment extends Fragment  {

    private View rootView;
    private BarChart graph;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        graph = rootView.findViewById(R.id.stats_mileage_graph);
        formatGraph();
        setGraphDataSet();
        return rootView;
    }

    /**
     *
     * @param entries List of entries for graph's dataSet
     * @param context
     * @param xAxisLabelValues
     * @return MileageGraphFragment with arguments
     */
    public static MileageGraphFragment newInstance(Context context, ArrayList<BarEntry> entries, String[] xAxisLabelValues) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(context.getString(R.string.graph_entries_key), entries);
        args.putStringArray(context.getString(R.string.graph_xlabel_key), xAxisLabelValues);

        MileageGraphFragment fragment = new MileageGraphFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Gets graph dataset from arguments and adds data onto graph.
     */
    private void setGraphDataSet(){
        List<BarEntry> entriesList = getArguments().getParcelableArrayList(getString(R.string.graph_entries_key));


        BarDataSet dataset = new BarDataSet(entriesList, "Distance");
        dataset = formatDataSet(dataset);

        graph.setData(new BarData(dataset));
        graph.invalidate();
    }

    /**
     * Formats dataset and sets styling.
     * @param dataSet
     * @return formatted dataSet
     */
    private BarDataSet formatDataSet(BarDataSet dataSet){
        dataSet.setValueTextSize(10);
        dataSet.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        return dataSet;
    }

    /**
     * Formats graph and sets styling.
     */
    private void formatGraph(){
        graph.getAxisLeft().setAxisMinimum(0);
        graph.getAxisLeft().setGranularity(2);
        graph.getAxisLeft().setSpaceTop(30);
        graph.getAxisRight().setEnabled(false);
        graph.getAxisRight().setDrawGridLines(false);
        graph.getAxisLeft().setDrawGridLines(false);

        graph.getXAxis().setGranularity(1);
        graph.getXAxis().setDrawGridLines(false);
        graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        String[] values = getArguments().getStringArray(getString(R.string.graph_xlabel_key));
        graph.getXAxis().setValueFormatter(new AxisValueFormatter(values));

        graph.animateXY(1500, 1500);
        graph.setTouchEnabled(false);

        graph.setDrawGridBackground(false);

        graph.setDescription(null);
    }

}
