package com.example.tobias.run.history;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.data.TrackedRunPredicates;
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
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

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
    private ArrayList<TrackedRun> trackedRuns;
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
        trackedRuns = new ArrayList<>();
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
//                trackedRuns.add(tr);
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
//                loadRecordsRecyclerView();
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

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackedRuns.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    trackedRuns.add(data.getValue(TrackedRun.class));
                }
                loadRecordsRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        adapter = new HistoryRecyclerViewAdapter(getContext(), trackedRuns, new HistoryRecyclerViewAdapter.OnOverflowButtonListener() {
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

    private void loadRecordsRecyclerView() {
        String filter = dateSpinner.getItems().get(dateSpinner.getSelectedIndex()).toString();
        switch (filter){
            case "Week" :
                adapter.updateItems(ConversionManager.filterRun(trackedRuns, TrackedRunPredicates.isRunFromWeek()));
                break;
            case "Month" :
                adapter.updateItems(ConversionManager.filterRun(trackedRuns, TrackedRunPredicates.isRunFromMonth()));
                break;
            case "Year" :
                adapter.updateItems(ConversionManager.filterRun(trackedRuns, TrackedRunPredicates.isRunFromYear()));
                break;
            case "All" :
                adapter.updateItems(trackedRuns);
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

    private void removeRunPermanently(TrackedRun trackedRun){
        trackedRuns.remove(trackedRun);
        databaseManager.deleteRun(trackedRun);
    }





}
