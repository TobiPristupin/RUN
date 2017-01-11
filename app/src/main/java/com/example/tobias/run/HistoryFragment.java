package com.example.tobias.run;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Fragment that displays actvities tracked, and can sort them by different criteria. Accesed via the DrawerLayout
 * in MainActivity as History.
 */
public class HistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View rootView;

    public HistoryFragment(){
        //Required empty constuctor.
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.date_spinner);
        //Spinner dropdown elements
        String[] categories = new String[]{"Week", "Month", "Year", "All"};

        //Create adapter for Spinner
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,
                categories);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);




        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }
}
