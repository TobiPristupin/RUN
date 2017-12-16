package com.example.tobias.run.history;


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

import com.example.tobias.run.R;
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.editor.EditorActivity;
import com.example.tobias.run.history.adapter.HistoryRecyclerViewAdapter;
import com.example.tobias.run.utils.VerticalDividerItemDecoration;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;

/**
 * Fragment that displays activities tracked, and can sort them by different criteria. Accessed via the DrawerLayout
 * in MainActivityView as History.
 */
public class HistoryFragmentView extends Fragment implements HistoryView {

    private View rootView;
    private HistoryRecyclerViewAdapter adapter;
    private ActionModeCallback modeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private HistoryPresenter presenter;

    private RecyclerView recyclerView;
    private MaterialSpinner dateSpinner;
    private View emptyViewImage;
    private View emptyViewHeader;
    private View emptyViewText;


    public HistoryFragmentView(){
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

        emptyViewImage = rootView.findViewById(R.id.history_empty_view_image);
        emptyViewHeader = rootView.findViewById(R.id.history_empty_view_header);
        emptyViewText = rootView.findViewById(R.id.history_empty_view_text);


        initRecyclerView();
        initDateSpinner();
        initFab();

        presenter = new HistoryPresenter(this, FirebaseDatabaseManager.getInstance());

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    private void initDateSpinner(){
        dateSpinner = (MaterialSpinner) rootView.findViewById(R.id.history_date_spinner);
        dateSpinner.setItems("Month", "Week", "Year", "All");
        dateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onSpinnerItemSelected();
            }
        });
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.history_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        VerticalDividerItemDecoration dividerItemDecoration = new VerticalDividerItemDecoration(40);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setItemAnimator(new OvershootInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(750);
        recyclerView.getItemAnimator().setRemoveDuration(500);
        adapter = new HistoryRecyclerViewAdapter(getContext(), new HistoryRecyclerViewAdapter.OnItemClicked() {

            @Override
            public void onClick(int position) {
                if (actionMode != null){
                    adapter.toggleSelection(position);
                    presenter.toggleItemSelection(adapter.getSelectedItems());
                }
            }

            @Override
            public boolean onLongClick(int position) {
                if (actionMode == null){
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(modeCallback);
                    setActiveActionModeBackground(true);
                }

                adapter.toggleSelection(position);
                presenter.toggleItemSelection(adapter.getSelectedItems());
                return true;
            }
        });


        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setDuration(500);
        animationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animationAdapter);
    }

    private void initFab(){
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.history_fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                startActivity(intent);
            }
        });

        //Hide fab when recycler view is scrolled in any direction
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    public void finishActionMode() {
        if (actionMode != null){
            actionMode.finish();
        }
    }

    @Override
    public void actionModeSetEditVisible(boolean visible) {
        if (visible){
            actionMode.getMenu().findItem(R.id.selected_item_menu_edit).setVisible(false);
        } else {
            actionMode.getMenu().findItem(R.id.selected_item_menu_edit).setVisible(true);
        }
    }

    @Override
    public void actionModeSetTitle(String title) {
        actionMode.setTitle(title);
    }

    @Override
    public void actionModeInvalidate() {
        actionMode.invalidate();
    }

    /**
     * Updates Recycler View's adapter data set with new data
     * @param data
     */
    @Override
    public void setData(List<Run> data) {
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
        adapter.updateItems(data);
    }

    /**
     * Dialog that asks the user for confirmation before deleting items.
     * @param selectedItems selected items in adapter to be deleted
     */
    @Override
    public void showDeleteDialog(final List<Integer> selectedItems) {
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
                presenter.onDeleteDialogYes(selectedItems);
            }
        });

        builder.create().show();
    }

    /**
     * @return current state of filter spinner
     */
    @Override
    public String getDataFilter() {
        return dateSpinner.getItems().get(dateSpinner.getSelectedIndex()).toString();
    }

    @Override
    public void showEmptyView(boolean longMessage) {
        if (longMessage){
            animateViews(true, emptyViewHeader, emptyViewImage, emptyViewText);
        } else {
            animateViews(true, emptyViewHeader, emptyViewImage);
        }
    }

    @Override
    public void removeEmptyView() {
        animateViews(false, emptyViewHeader, emptyViewImage, emptyViewText);
    }

    @Override
    public void sendIntentEditorActivity(@Nullable Run runToEdit) {
        Intent intent = new Intent(getContext(), EditorActivity.class);

        if (runToEdit != null){
            intent.putExtra(getString(R.string.run_intent_key), runToEdit);
        }

        startActivity(intent);
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
     * Sets activated background color for DateSpinner, SpinnerLayout and status bar (API + 19) when action mode is created.
     */
    private void setActiveActionModeBackground(boolean activated){
        RelativeLayout spinnerLayout = rootView.findViewById(R.id.history_spinner_layout);

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
                    presenter.onDeleteMenuClicked(adapter.getSelectedItems());
                    mode.finish();
                    return true;

                case R.id.selected_item_menu_edit :
                    presenter.onEditMenuClicked(adapter.getSelectedItems());
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
