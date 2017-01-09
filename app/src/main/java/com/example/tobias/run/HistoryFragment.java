package com.example.tobias.run;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * HistoryFragment is displayed when selecting {@code History} tab. Displays all previously tracked activites, allows users to sort
 * them and add new activities, as well as accessing previous activities and modyfing/deleting them.
 * tracked as well as graphs to support them.
 */
public class HistoryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

}
