package com.example.tobias.run.stats.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tobias.run.R;
import com.example.tobias.run.utils.AxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;


/**
 * Graph cannot be initialized until fragment is attached because it needs context. Class has to implement
 * setDataset and setLabelValues methods, which may be called before fragment is attached when graph is null.
 * To workaround it, values passed in methods are stored in variables, and only added to graph once fragments has been attached
 * and graph is not null.
 */
public class MileageGraphFragment extends Fragment implements GraphFragment {

    private View rootView;
    private BarChart graph;
    private List<BarEntry> dataSet = new ArrayList<>();
    private String[] labelValues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        graph = new BarChart(getContext());
        LinearLayout layout = rootView.findViewById(R.id.stats_graph_layout);
        graph.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(graph);

        styleGraph();
        return rootView;
    }

    @Override
    public void setDataSet(List<Entry> entries) {
        for (Entry entry : entries){
            dataSet.add(new BarEntry(entry.getX(), entry.getY()));
        }
    }

        /**
     *
     * @param entries List of entries for graph's dataSet
     * @param context
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
     * Formats graph and sets styling.
     */
    private void styleGraph(){
        BarDataSet data = new BarDataSet(dataSet, "Distance");
        data.setValueTextSize(10);
        data.setColor(getContext().getResources().getColor(R.color.colorPrimary));

        graph.setData(new BarData(data));
        graph.invalidate();

        graph.getAxisLeft().setAxisMinimum(0);
        graph.getAxisLeft().setGranularity(2);
        graph.getAxisLeft().setSpaceTop(30);
        graph.getAxisRight().setEnabled(false);
        graph.getAxisRight().setDrawGridLines(false);
        graph.getAxisLeft().setDrawGridLines(true);

        graph.getXAxis().setGranularity(1);
        graph.getXAxis().setDrawGridLines(false);
        graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        graph.getXAxis().setValueFormatter(new AxisValueFormatter(labelValues));

        graph.animateXY(1500, 1500);
        graph.setTouchEnabled(false);

        graph.setDrawGridBackground(false);

        graph.setDescription(null);
    }

}
