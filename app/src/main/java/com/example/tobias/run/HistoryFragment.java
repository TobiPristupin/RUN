package com.example.tobias.run;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.joda.time.Period;

import java.util.ArrayList;

/**
 * Fragment that displays actvities tracked, and can sort them by different criteria. Accesed via the DrawerLayout
 * in MainActivity as History.
 */
public class HistoryFragment extends Fragment {

    private View rootView;

    public HistoryFragment(){
        //Required empty constuctor.
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        initSpinner();
        initListView();
        return rootView;
    }

    /**
     * Populates spinner, implements spinner callbacks and small runtime UI tweaks on spinner.
     */
    private void initSpinner(){
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.date_spinner);
        //Spinner dropdown elements
        String[] categories = new String[]{"Week", "Month", "Year", "All"};
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
                TextView selectedText = (TextView) spinner.getSelectedView();
                selectedText.setTextColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initListView(){
        ListView listView = (ListView) rootView.findViewById(R.id.history_listview);
        ArrayList<HistoryListItem> trackedRuns = getTrackedRuns();
        HistoryListItemAdapter adapter = new HistoryListItemAdapter(getContext(), trackedRuns);
        listView.setAdapter(adapter);
    }

    /**
     * Gets all tracked runs from sqlite Database, add thems in Arrraylist and returns them.
     * @return ArrayList<historyListItems> trackedRuns
     */
    private ArrayList<HistoryListItem> getTrackedRuns(){
        //TODO: Get Data from Db
        ArrayList<HistoryListItem> trackedRuns = new ArrayList<>();
        trackedRuns.add(new HistoryListItem(2, new Period(0, 22, 43, 0), new DateTime(2017, 1, 12, 0, 0), 2));

        return trackedRuns;
    }

}
