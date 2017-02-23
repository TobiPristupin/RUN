package com.example.tobias.run;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Fragment that displays activities tracked, and can sort them by different criteria. Accesed via the DrawerLayout
 * in MainActivity as History.
 */
public class HistoryFragment extends Fragment {

    private View rootView;
    private HistoryListItemAdapter adapter;
    private ListView listView;

    public HistoryFragment(){
        //Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        initDateSpinner();
        initListView();
        initFab();
        initTopBar();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecords();
    }

    /**
     * Populates date spinner, implements spinner callbacks and small runtime UI tweaks on spinner.
     */
    private void initDateSpinner(){
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.date_spinner);
        //Spinner dropdown elements
        String[] categories = new String[]{"All", "Week", "Month", "Year"};
        //Create adapter for Spinner
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,
                categories);

        spinner.setAdapter(spinnerAdapter);
        /*Set color of spinner dropdown icon to white. Because of added complexity when designing a custom
        layout file for the Spinner, decided to instead change the color on runtime*/
        spinner.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Get currentlySelected TextView and set its color to white.Because of added complexity
                when designing a custom layout file for the Spinner, decided to instead change the color on runtime*/
                TextView selectedTextView = (TextView) spinner.getSelectedView();
                if(selectedTextView != null){
                    selectedTextView.setTextColor(Color.parseColor("#FFFFFF"));
                }
                //Reload records into listview with new value set.
                loadRecords();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initListView(){
        listView = (ListView) rootView.findViewById(R.id.history_listview);
        ArrayList<TrackedRun> trackedRuns = new DatabaseHandler(getContext()).getAllTrackedRuns();
        adapter = new HistoryListItemAdapter(getContext(), trackedRuns, this, new HistoryListItemAdapter.OnOverflowButtonListener() {
            @Override
            public void onDeleteClick(TrackedRun tr) {
                showDeleteDialog(tr);
            }

            @Override
            public void onEditClick(TrackedRun tr) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                intent.putExtra("TrackedRun", tr);
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

    }


    /**
     * Sets Floating action Button callback
     */
    private void initFab(){
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Retrieves records from db according to dateSpinner value and populates listview.
     * Because of countless bugs trying to add items dynamically,
     * resorted to clearing the adapter and adding all the items again, which is not great for performance,
     * and should eventually be refactored to a more efficient solution.
     */
    private void loadRecords() {
        //TODO: Reformat for performance
        Spinner dateSpinner = (Spinner) rootView.findViewById(R.id.date_spinner);
        String sortBy = dateSpinner.getSelectedItem().toString();
        adapter.clear();

        switch (sortBy){
            case "All" :
                for(TrackedRun tr : new DatabaseHandler(getContext()).getAllTrackedRuns()){
                    adapter.add(tr);
                }
                adapter.notifyDataSetChanged();
                break;

            case "Week" :
                for(TrackedRun tr : new DatabaseHandler(getContext()).getWeekTrackedRuns()){
                    adapter.add(tr);
                }
                adapter.notifyDataSetChanged();
                break;

            case "Month" :
                for(TrackedRun tr : new DatabaseHandler(getContext()).getMonthTrackedRuns()){
                    adapter.add(tr);
                }
                adapter.notifyDataSetChanged();
                break;

            case "Year" :
                for(TrackedRun tr : new DatabaseHandler(getContext()).getYearTrackedRuns()){
                    adapter.add(tr);
                }
                adapter.notifyDataSetChanged();
                break;
        }

    }

    private void initTopBar(){
        TextView currentMonthText = (TextView) rootView.findViewById(R.id.current_month_text);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMM d");
        currentMonthText.setText(formatter.print(new DateTime()));
    }

    private void showDeleteDialog(TrackedRun tr){
        final TrackedRun trackedRun = tr;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteListItem(trackedRun);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    private void deleteListItem(TrackedRun tr){
        new DatabaseHandler(getContext()).deleteItem(tr.getId());
        loadRecords();
    }

}
