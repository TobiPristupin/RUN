package com.example.tobias.run.stats.personalrecords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tobias.run.R;
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.PersonalRecord;
import com.example.tobias.run.utils.VerticalDividerItemDecoration;

import java.util.List;

/**
 * Created by Tobi on 11/8/2017.
 */

public class StatsFragmentPrsView extends Fragment implements StatsPrsView {

    private View rootView;
    private StatsPrsPresenter presenter;
    private PrsRecyclerViewAdapter adapter;

    public StatsFragmentPrsView() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_prs, container, false);

        presenter = new StatsPrsPresenter(this, FirebaseDatabaseManager.getInstance());
        adapter = new PrsRecyclerViewAdapter(getContext());
        initRecyclerView();

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    @Override
    public void setData(List<PersonalRecord> personalRecordsList) {
        adapter.updateItems(personalRecordsList);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = rootView.findViewById(R.id.stats_prs_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        VerticalDividerItemDecoration dividerItemDecoration = new VerticalDividerItemDecoration(20);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }



}
