package com.example.tobias.run.history;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.database.FirebaseDatabaseManager;
import com.example.tobias.run.database.TrackedRun;
import com.example.tobias.run.editor.EditorActivity;
import com.example.tobias.run.history.adapter.HistoryRecyclerViewAdapter;
import com.example.tobias.run.utils.ConversionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private HistoryRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Spinner spinner;
    private ArrayList<TrackedRun> trackedRunsToDisplay; //Tracked runs that should be displayed according to dateSpinner
    private ArrayList<TrackedRun> allTrackedRuns; //All tracked runs retrieved from database.
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDatabaseManager databaseManager;
    private DatabaseReference databaseRef;

    public HistoryFragment(){
        //Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = firebaseDatabase.getReference("users/" + firebaseUser.getUid());
        trackedRunsToDisplay = new ArrayList<>();
        allTrackedRuns = new ArrayList<>();
        databaseManager = new FirebaseDatabaseManager();

        initDateSpinner();
        initRecyclerView();
        initFab();
        initTopBar();

        return rootView;
    }

    /**
     * Populates date spinner, implements spinner callbacks and small runtime UI tweaks on spinner.
     */
    private void initDateSpinner(){
        spinner = (Spinner) rootView.findViewById(R.id.date_spinner);

        //Spinner dropdown elements
        String[] categories = new String[]{"Month", "Week", "Year", "All"};
        //Create adapter for Spinner
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,
                categories);

        spinner.setAdapter(spinnerAdapter);

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
                loadRecordsListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void initRecyclerView(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.history_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HistoryRecyclerViewAdapter(getContext(), trackedRunsToDisplay);
        recyclerView.setAdapter(adapter);

        //TODO: Add empty view

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                *Can't check if run has already been added to trackedRunsToDisplay using trackedRunsToDisplay.contains() because firebase database always creates a new
                *object with the retrieved data. The data fields may already have been added to trackedRunsToDisplay but.contains() returns false because
                *its a different object with same data. Workaround is to clear tracked runs .
                */
                allTrackedRuns.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    TrackedRun tr = data.getValue(TrackedRun.class);
                    allTrackedRuns.add(tr);
                }
                loadRecordsListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /**
     * Sets Floating action Button callback
     */
    private void initFab(){
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.history_fab_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Filters All Tracked Runs according to date spinner and adds them to tracked runs to display.
     */
    private void loadRecordsListView() {
        String sortBy = spinner.getSelectedItem().toString();
        trackedRunsToDisplay.clear();
        for (TrackedRun tr : allTrackedRuns){
            switch (sortBy){
                case "All" :
                    trackedRunsToDisplay.add(tr);
                    break;
                case "Week" :
                    System.out.println(tr.getDistanceMiles());
                    System.out.println(tr.getDate());
                    System.out.println(ConversionManager.getStartOfWeek());
                    System.out.println(ConversionManager.getEndOfWeek());
                    if (tr.getDate() >= ConversionManager.getStartOfWeek() && tr.getDate() <= ConversionManager.getEndOfWeek()){
                        trackedRunsToDisplay.add(tr);
                    }
                    break;
                case "Month" :
                    if (tr.getDate() >= ConversionManager.getStartOfMonth() && tr.getDate() <= ConversionManager.getEndOfMonth()){
                        trackedRunsToDisplay.add(tr);
                    }
                    break;
                case "Year" :
                    if (tr.getDate() >= ConversionManager.getStartOfYear() && tr.getDate() <= ConversionManager.getEndOfMonth()){
                        trackedRunsToDisplay.add(tr);
                    }
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initTopBar(){
        TextView currentMonthText = (TextView) rootView.findViewById(R.id.history_date_text);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMM d");
        currentMonthText.setText(formatter.print(new DateTime()));
    }


    private void showDeleteDialog(final TrackedRun tr){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseManager.deleteRun(tr);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


}
