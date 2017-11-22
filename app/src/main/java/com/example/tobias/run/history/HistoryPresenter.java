package com.example.tobias.run.history;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.RunPredicates;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.RunUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 10/20/2017.
 */

public class HistoryPresenter implements Observer<List<Run>>{


    private HistoryView view;
    private List<Run> runList = new ArrayList<>();
    private ObservableDatabase<Run> model;

    public HistoryPresenter(HistoryView view, ObservableDatabase<Run> model){
        this.view = view;
        this.model = model;
        this.model.startQuery();
        model.attachObserver(this);
    }

    public void onDetachView(){
        //Desubscribe from model observers list
        model.detachObserver(this);
    }

    /**
     * Called from model to to update data
     * @param data
     */
    @Override
    public void updateData(List<Run> data) {
        runList.clear();
        runList.addAll(data);
        updateViewData();
    }

    public void onSpinnerItemSelected(){
        updateViewData();
    }

    /**
     * Filters current data according to view's filter and sends it to the view.
     * Then finishes action mode in view and shows the view's empty view if needed.
     */
    public void updateViewData(){
        String filter = view.getDataFilter();
        List<Run> filteredRuns = new ArrayList<>();

        switch (filter){
            case "Week" :
                filteredRuns.addAll(RunUtils.filterList(runList, RunPredicates.isRunFromWeek()));
                break;
            case "Month" :
                filteredRuns.addAll(RunUtils.filterList(runList, RunPredicates.isRunFromMonth()));
                break;
            case "Year" :
                filteredRuns.addAll(RunUtils.filterList(runList, RunPredicates.isRunFromYear()));
                break;
            case "All" :
                filteredRuns.addAll(runList);
        }

        view.setData(filteredRuns);
        //Exit selection state, user isn't allowed to change filter with selected items.
        view.finishActionMode();

        if (shouldShowEmptyView()){
            //If presenters's data set is empty, meaning that database has no items, message shown changes
            boolean longMessage = runList.isEmpty();
            view.showEmptyView(longMessage);
        } else {
            view.removeEmptyView();
        }
    }

    private boolean shouldShowEmptyView(){
        return view.adapterIsDataSetEmpty();
    }

    /**
     * Toggle the selection state of an item
     *
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode in historyView must not be null).
     */
    public void toggleItemSelection(List<Integer> selectedItems){
        int selectedItemCount = selectedItems.size();
        if (selectedItemCount == 0){
            view.finishActionMode();
            return;
        }

        //Can't use edit functionality when more than on item is selected so disable.
        if (selectedItemCount > 1){
            view.actionModeSetEditVisible(true);
        } else {
            view.actionModeSetEditVisible(false);
        }

        view.actionModeSetTitle(String.valueOf(selectedItemCount));
        view.actionModeInvalidate();
    }

    public void deleteRun(Run run){
        model.remove(run);
    }

    public void deleteRun(List<Integer> indexList){
        for (Integer index : indexList){
            model.remove(runList.get(index));
        }
    }

    public void onDeleteMenuClicked(List<Integer> selectedItems){
        view.showDeleteDialog(selectedItems);
    }

    public void onDeleteDialogYes(List<Integer> selectedItems){
        deleteRun(selectedItems);
    }

    public void onEditMenuClicked(List<Integer> selectedItems){
        /*Get selected tracked run from runList. Always get first item of adapter's selected
        items because edit functionality will only be accessible to user when only one run is selected,
        so only one item will be in adapter's selected items.*/
        Run run = runList.get(selectedItems.get(0));
        //View will handle if run is null.
        view.sendIntentEditorActivity(run);
    }



}
