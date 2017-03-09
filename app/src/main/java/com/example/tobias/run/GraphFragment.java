package com.example.tobias.run;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tobi on 3/7/17.
 */

public class GraphFragment extends Fragment {

    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.stats_container, container, false);
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
            case  BAR_GRAPH_MILEAGE_MONTH:

        }
    }


}
