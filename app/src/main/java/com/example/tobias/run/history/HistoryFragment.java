package com.example.tobias.run.history;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.tobias.run.R;
import com.example.tobias.run.database.FirebaseDatabaseManager;
import com.example.tobias.run.database.TrackedRun;
import com.example.tobias.run.editor.EditorActivity;
import com.example.tobias.run.history.adapter.HistoryListItemAdapter;
import com.example.tobias.run.utils.DateManager;
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
    private HistoryListItemAdapter adapter;
    private ListView listView;
    private Spinner spinner;
    private ArrayList<TrackedRun> trackedRuns;
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
        trackedRuns = new ArrayList<>();
        databaseManager = new FirebaseDatabaseManager();

        initDateSpinner();
        initListView();
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


    private void initListView(){
        listView = (ListView) rootView.findViewById(R.id.history_listview);

        adapter = new HistoryListItemAdapter(getContext(), new HistoryListItemAdapter.OnOverflowButtonListener() {
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

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                *Can't check if run has already been added to trackedRuns using trackedRuns.contains() because firebase database always creates a new
                *object with the retrieved data. The data fields may already have been added to trackedRuns but.contains() returns false because
                *its a different object with same data. Workaround is to clear tracked runs .
                */
                trackedRuns.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    TrackedRun tr = data.getValue(TrackedRun.class);
                    trackedRuns.add(tr);
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
     * Filters Tracked Runs according to date spinner and adds them to ListView
     */
    private void loadRecordsListView() {
        adapter.clear();
        String sortBy = spinner.getSelectedItem().toString();
        for (TrackedRun tr : trackedRuns){
            switch (sortBy){
                case "All" :
                    adapter.add(tr);
                    break;
                case "Week" :
                    if (tr.getDate() >= DateManager.getStartOfWeek() && tr.getDate() <= DateManager.getEndOfWeek()){
                        adapter.add(tr);
                    }
                    break;
                case "Month" :
                    if (tr.getDate() >= DateManager.getStartOfMonth() && tr.getDate() <= DateManager.getEndOfMonth()){
                        adapter.add(tr);
                    }
                    break;
                case "Year" :
                    if (tr.getDate() >= DateManager.getStartOfYear() && tr.getDate() <= DateManager.getEndOfMonth()){
                        adapter.add(tr);
                    }
                    break;
            }
        }
    }

    private void initTopBar(){
        TextView currentMonthText = (TextView) rootView.findViewById(R.id.current_month_text);
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
