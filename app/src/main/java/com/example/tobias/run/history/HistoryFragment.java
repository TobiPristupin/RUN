package com.example.tobias.run.history;


import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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
import java.util.List;

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
    private ArrayList<TrackedRun> trackedRuns =  new ArrayList<>();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseDatabaseManager databaseManager = new FirebaseDatabaseManager();
    private DatabaseReference databaseRef;
    private ActionModeCallback modeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private RelativeLayout spinnerLayout;

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

        firebaseUser = firebaseAuth.getCurrentUser();
        databaseRef = firebaseDatabase.getReference("users/" + firebaseUser.getUid());
        spinnerLayout = rootView.findViewById(R.id.history_spinner_layout);

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
        VerticalDividerItemDecoration dividerItemDecoration = new VerticalDividerItemDecoration(40);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setItemAnimator(new OvershootInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(750);
        recyclerView.getItemAnimator().setRemoveDuration(500);
        adapter = new HistoryRecyclerViewAdapter(getContext(), trackedRuns, new HistoryRecyclerViewAdapter.OnItemClicked() {
            @Override
            public void onClick(int position) {
                if (actionMode != null){
                    toggleSelection(position);
                }
            }

            @Override
            public boolean onLongClick(int position) {
                if (actionMode == null){
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(modeCallback);
                    setActiveActionModeBackground(true);
                }

                toggleSelection(position);
                return true;
            }
        });


        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setDuration(500);
        animationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animationAdapter);
    }

    /**
     * Toggle the selection state of an item.
     *
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     */
    private void toggleSelection(int position){
        adapter.toggleSelection(position);
        int selectedItemCount = adapter.getSelectedItemCount();

        if (selectedItemCount == 0) {
            actionMode.finish();
            return;
        }

        //Can't use edit functionality when more than on item is selected so disable.
        if (selectedItemCount > 1){
            actionMode.getMenu().findItem(R.id.selected_item_menu_edit).setVisible(false);
        } else {
            actionMode.getMenu().findItem(R.id.selected_item_menu_edit).setVisible(true);
        }

        actionMode.setTitle(String.valueOf(selectedItemCount));
        actionMode.invalidate();
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

        //avoid user changing filter with runs selected previously
        if (actionMode != null){
            actionMode.finish();
        }

        //Show empty if adapter dataset is empty, show all text of empty view if zero runs have been added.
        recyclerViewShowEmptyView(adapter.isDatasetEmpty(), trackedRuns.isEmpty());
    }

    private void animateViews(boolean shouldShow, View... views){
        if (shouldShow){
            for (View view: views){
                view.setVisibility(View.VISIBLE);
                view.animate().setDuration(800).setStartDelay(500).alpha(1.0f);
            }
        } else {
            for (View view: views){
                view.setVisibility(View.GONE);
                view.animate().setDuration(700).alpha(0.0f);
            }
        }

    }

    /**
     * Shows recycler view empty view
     * @param shouldShow true if should show message, false if should remove message
     * @param longMessage true if should show long text description, false if shouldn't
     */
    private void recyclerViewShowEmptyView(boolean shouldShow, boolean longMessage){
        View emptyViewImage = rootView.findViewById(R.id.history_empty_view_image);
        View emptyViewHeader = rootView.findViewById(R.id.history_empty_view_header);
        View emptyViewText = rootView.findViewById(R.id.history_empty_view_text);

        if (longMessage){
            animateViews(shouldShow, emptyViewHeader, emptyViewImage, emptyViewText);
        } else {
            animateViews(shouldShow, emptyViewHeader, emptyViewImage);
        }
    }

    private void initTopBar(){
        TextView currentMonthText = (TextView) rootView.findViewById(R.id.history_date_text);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMM d");
        currentMonthText.setText(formatter.print(new DateTime()));
    }

    /**
     * Sets activated background color for DateSpinner, SpinnerLayout and status bar (API + 19) when action mode is created.
     */
    private void setActiveActionModeBackground(boolean activated){
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (activated){
            spinnerLayout.setBackgroundColor(getResources().getColor(R.color.actionMode));
            dateSpinner.setBackgroundColor(getResources().getColor(R.color.actionMode));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
        } else {
            spinnerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            dateSpinner.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            }
        }

    }

    private void showDeleteDialog(final List<Integer> selectedItems){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");

        if (selectedItems.size() > 1){
            builder.setMessage("Are you sure you want to delete " + selectedItems.size() + " items? You won't be able to recover them.");
        } else {
            builder.setMessage("Are you sure you want to delete one item? You won't be able to recover it.");
        }

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRunsPermanently(getTrackedRunsFromIndex(selectedItems));
            }
        });

        builder.create().show();
    }

    private void deleteRunsPermanently(ArrayList<TrackedRun> trackedRunsToDelete){
        for (TrackedRun tr : trackedRunsToDelete){
            trackedRuns.remove(tr);
            databaseManager.deleteRun(tr);
        }
    }

    private ArrayList<TrackedRun> getTrackedRunsFromIndex(List<Integer> indexList){
        ArrayList<TrackedRun> arrayList = new ArrayList<>();
        for (Integer index : indexList){
            arrayList.add(this.trackedRuns.get(index));
        }
        return arrayList;
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.history_selected_item_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.selected_item_menu_delete:
                    showDeleteDialog(adapter.getSelectedItems());
                    mode.finish();
                    return true;

                case R.id.selected_item_menu_edit :
                    Intent intent = new Intent(getContext(), EditorActivity.class);
                    ArrayList<TrackedRun> trackedRuns = getTrackedRunsFromIndex(adapter.getSelectedItems());
                    /*Selected items size will always be 1 because when more than one run is selected edit
                    functionality is disabled when selected items > 1.*/
                    TrackedRun tr = getTrackedRunsFromIndex(adapter.getSelectedItems()).get(0);
                    intent.putExtra(getString(R.string.trackedrun_intent_key), tr);
                    startActivity(intent);

                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            setActiveActionModeBackground(false);
            adapter.clearSelection();
            actionMode = null;
        }
    }


}
