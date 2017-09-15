package com.example.tobias.run.history;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.ItemDecoration.*;

import com.example.tobias.run.R;
import com.example.tobias.run.database.FirebaseDatabaseManager;
import com.example.tobias.run.database.TrackedRun;
import com.example.tobias.run.editor.EditorActivity;
import com.example.tobias.run.history.adapter.HistoryRecyclerViewAdapter;
import com.example.tobias.run.utils.ConversionManager;
import com.example.tobias.run.utils.VerticalDividerItemDecoration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Iterator;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;

/**
 * Fragment that displays activities tracked, and can sort them by different criteria. Accessed via the DrawerLayout
 * in MainActivity as History.
 */
public class HistoryFragment extends Fragment {

    private View rootView;
    private HistoryRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MaterialSpinner dateSpinner;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("History");
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

        initRecyclerView();
        initFirebaseDatabase();
        initDateSpinner();
        initFab();
        initTopBar();

        return rootView;
    }

    private void initDateSpinner(){
        dateSpinner = (MaterialSpinner) rootView.findViewById(R.id.history_date_spinner);
        dateSpinner.setEllipsize(TextUtils.TruncateAt.START);
        dateSpinner.setItems("Month", "Week", "Year", "All");
        dateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                loadRecordsRecyclerView();
            }
        });

    }

    private void initFirebaseDatabase(){
//        databaseRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                TrackedRun tr = dataSnapshot.getValue(TrackedRun.class);
//                if (!containsRun(allTrackedRuns, tr)){
//                    allTrackedRuns.add(tr);
//                }
//                loadRecordsRecyclerView();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        databaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                allTrackedRuns.clear();
//                for (DataSnapshot data : dataSnapshot.getChildren()){
//                    TrackedRun tr = data.getValue(TrackedRun.class);
//                    allTrackedRuns.add(tr);
//                }
//                loadRecordsRecyclerView();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.history_recyclerview);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        VerticalDividerItemDecoration dividerItemDecoration = new VerticalDividerItemDecoration(30);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setItemAnimator(new OvershootInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(500);
        recyclerView.getItemAnimator().setRemoveDuration(500);

        adapter = new HistoryRecyclerViewAdapter(getContext(), trackedRunsToDisplay, new HistoryRecyclerViewAdapter.OnOverflowButtonListener() {
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
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setDuration(500);
        animationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animationAdapter);

        //TODO: Add empty view
    }

    /**
     * Sets Floating action Button callback
     */
    private void initFab(){
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.history_fab_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                //Shared transition animations not implemented below api 21
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    String transitionName = fab.getTransitionName();
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                            (View) fab, transitionName);
                    startActivity(intent, activityOptions.toBundle());
                } else {
                    startActivity(intent);
                }

            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE :
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fab.show();
                            }
                        }, 500);
                        break;
                    default :
                        fab.hide();
                        break;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    /**
     * Add all tracked runs according to date spinner criteria. If run doesn't meet new criteria
     * and has been previously added, remove.
     */
    private void loadRecordsRecyclerView() {
        String sortBy = dateSpinner.getItems().get(dateSpinner.getSelectedIndex()).toString();
        trackedRunsToDisplay.clear();
        for (TrackedRun tr : allTrackedRuns){
            switch (sortBy){
                case "All" :
                    if (!containsRun(trackedRunsToDisplay, tr)){
                        addRun(tr);
                    }
                    break;
                case "Week" :
                    //If run meets date criteria and hasn't been added previously then add.
                    if (tr.getDate() >= ConversionManager.getStartOfWeek() && tr.getDate() <= ConversionManager.getEndOfWeek()){
                        if (!containsRun(trackedRunsToDisplay, tr)){
                            addRun(tr);
                        }
                    } else { //If run doesn't meet criteria and has been added, remove.
                        if (containsRun(trackedRunsToDisplay, tr)){
                            removeRunFromDisplay(tr);
                        }
                    }
                    break;
                case "Month" :
                    if (tr.getDate() >= ConversionManager.getStartOfMonth() && tr.getDate() <= ConversionManager.getEndOfMonth()) {
                        if (!containsRun(trackedRunsToDisplay, tr)) {
                            addRun(tr);
                        }
                    } else {
                        if (containsRun(trackedRunsToDisplay, tr)) {
                            removeRunFromDisplay(tr);
                        }
                    }

                    break;
                case "Year" :
                    if (tr.getDate() >= ConversionManager.getStartOfYear() && tr.getDate() <= ConversionManager.getEndOfYear()){
                        if (!containsRun(trackedRunsToDisplay, tr)){
                            addRun(tr);
                        }
                    } else {
                        if (containsRun(trackedRunsToDisplay, tr)){
                            removeRunFromDisplay(tr);
                        }
                    }
                    break;
            }
        }
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
                removeRunPermanently(tr);
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

    /**
     * Checks if TrackedRun is in ArrayList. Different from ArrayList .contains() method because containsRun
     * compares if ID of run is the same, while contains compares if the object has the same reference, which may result
     * in an incorrect result when Firebase Database returns a new object reference, but with the same data.
     * @param arrayList
     * @param trackedRun
     * @return
     */
    private boolean containsRun(ArrayList<TrackedRun> arrayList, TrackedRun trackedRun){
        for (TrackedRun tr : arrayList){
            if (tr.getId().equals(trackedRun.getId())){
                return true;
            }
        }
        return false;
    }

    private TrackedRun getRunFromId(ArrayList<TrackedRun> arrayList, String id){
        for (TrackedRun tr : arrayList){
            if (tr.getId().equals(id)){
                return tr;
            }
        }
        return null;
    }


    private void addRun(TrackedRun trackedRun){
        trackedRunsToDisplay.add(trackedRun);
        adapter.notifyItemInserted(trackedRunsToDisplay.indexOf(trackedRun));
    }

    /**
     * Removes run from trackedRunsToDisplay, notifies adapter its removal. Does not remove
     * from database.
     * @param trackedRun
     */
    private void removeRunFromDisplay(TrackedRun trackedRun){
        int index = trackedRunsToDisplay.indexOf(trackedRun);
        trackedRunsToDisplay.remove(trackedRun);
        adapter.notifyItemRemoved(index);
    }

    /**
     * Removes run from database.
     * @param trackedRun
     */
    private void removeRunPermanently(TrackedRun trackedRun){
        allTrackedRuns.remove(trackedRun);
        int index = trackedRunsToDisplay.indexOf(trackedRun);
        trackedRunsToDisplay.remove(trackedRun);
        adapter.notifyItemRemoved(index);
        databaseManager.deleteRun(trackedRun);
    }





}
