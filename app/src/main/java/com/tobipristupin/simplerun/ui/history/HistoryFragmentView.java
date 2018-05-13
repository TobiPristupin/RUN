package com.tobipristupin.simplerun.ui.history;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.RelativeLayout;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseFragment;
import com.tobipristupin.simplerun.data.repository.FirebaseRepository;
import com.tobipristupin.simplerun.data.repository.SharedPrefRepository;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.data.model.RunFilter;
import com.tobipristupin.simplerun.ui.VerticalDividerItemDecoration;
import com.tobipristupin.simplerun.ui.editor.EditorActivityView;
import com.tobipristupin.simplerun.ui.history.adapter.HistoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;

/**
 * Fragment that displays activities tracked, and can sort them by different criteria. Accessed via the DrawerLayout
 * in MainActivityView as History.
 */

public class HistoryFragmentView extends BaseFragment implements HistoryView {

    private View rootView;
    private HistoryRecyclerViewAdapter adapter;
    private ActionModeCallback modeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private HistoryPresenter presenter;
    private DrawerLayout drawerLayout;
    private DrawerLayout.DrawerListener drawerListener;

    private RecyclerView recyclerView;
    private MaterialSpinner dateSpinner;
    private View emptyViewImage;
    private View emptyViewHeader;

    private List<String> spinnerItems = new ArrayList<>();


    public HistoryFragmentView(){
        //Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        emptyViewImage = rootView.findViewById(R.id.history_empty_view_image);
        emptyViewHeader = rootView.findViewById(R.id.history_empty_view_header);

        initDateSpinner();
        initRecyclerView();
        initFab();
        initNavigationDrawer();

        presenter = new HistoryPresenter(this, new FirebaseRepository());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.all_history);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStartView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        drawerLayout.removeDrawerListener(drawerListener);
    }

    private void initNavigationDrawer(){
        drawerLayout = getActivity().findViewById(R.id.main_activity_drawer_layout);
        drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                finishActionMode();
            }

            @Override
            public void onDrawerOpened(View drawerView) {}

            @Override
            public void onDrawerClosed(View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        };

        drawerLayout.addDrawerListener(drawerListener);
    }

    private void initDateSpinner(){
        dateSpinner = rootView.findViewById(R.id.history_date_spinner);

        spinnerItems.addAll(Arrays.asList(RunFilter.MONTH.toStringLocalized(getResources()),
                RunFilter.WEEK.toStringLocalized(getResources()),
                RunFilter.YEAR.toStringLocalized(getResources()),
                RunFilter.ALL.toStringLocalized(getResources())));

        dateSpinner.setItems(spinnerItems);

        dateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onSpinnerItemSelected();
            }
        });
    }


    private void initRecyclerView(){
        recyclerView = rootView.findViewById(R.id.history_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        VerticalDividerItemDecoration dividerItemDecoration = new VerticalDividerItemDecoration(40);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setItemAnimator(new OvershootInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(750);
        recyclerView.getItemAnimator().setRemoveDuration(500);


        adapter = new HistoryRecyclerViewAdapter(getContext(), getAdapterOnItemClicked(), new SharedPrefRepository(getContext()));


        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setDuration(500);
        animationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animationAdapter);
    }

    private HistoryRecyclerViewAdapter.OnItemClicked getAdapterOnItemClicked(){
        return new HistoryRecyclerViewAdapter.OnItemClicked() {

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
                }

                adapter.toggleSelection(position);
                presenter.toggleItemSelection(adapter.getSelectedItems());
                return true;
            }
        };
    }

    private void initFab(){
        final FloatingActionButton fab = rootView.findViewById(R.id.history_fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditorActivityView.class);
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

    @Override
    public void setData(List<Run> data) {
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
        adapter.updateItems(data);
    }

    /**
     * @return current state of filter spinner
     */
    @Override
    public RunFilter getDataFilter() {
        String value = dateSpinner.getItems().get(dateSpinner.getSelectedIndex()).toString();
        return RunFilter.fromString(value, getResources());
    }

    @Override
    public void setSpinnerSelectedItem(RunFilter filter) {
        dateSpinner.setSelectedIndex(spinnerItems.indexOf(filter.toStringLocalized(getResources())));
    }

    @Override
    public void showEmptyView() {
        animateViews(true, emptyViewHeader, emptyViewImage);
    }

    @Override
    public void removeEmptyView() {
        animateViews(false, emptyViewHeader, emptyViewImage);
    }

    /**
     * Dialog that asks the user for confirmation before deleting items.
     * @param selectedItems selected items in adapter to be deleted
     */
    @Override
    public void showDeleteDialog(final List<Integer> selectedItems) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String title = getString(R.string.history_fragment_view_dialog_title);
        builder.setTitle(title);

        String text = getResources().getQuantityString(R.plurals.history_fragment_view_dialog_text, selectedItems.size(), selectedItems.size());
        builder.setMessage(text);

        String negativeText = getString(R.string.history_fragment_view_cancel);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        String positiveText = getString(R.string.history_fragment_view_yes);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.onDeleteDialogYes(selectedItems);
            }
        });

        builder.create().show();
    }

    @Override
    public void sendIntentEditorActivity(@Nullable Run runToEdit) {
        Intent intent = new Intent(getContext(), EditorActivityView.class);

        if (runToEdit != null){
            intent.putExtra(EditorActivityView.INTENT_KEY, runToEdit);
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

        if (activated){
            spinnerLayout.setBackgroundColor(getResources().getColor(R.color.actionMode));
            dateSpinner.setBackgroundColor(getResources().getColor(R.color.actionMode));
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        } else {
            spinnerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            dateSpinner.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
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
            setActiveActionModeBackground(true);
            return true;
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
