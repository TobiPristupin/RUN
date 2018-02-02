package com.example.tobias.run.history;

import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.RunPredicates;
import com.example.tobias.run.interfaces.Observer;
import com.example.tobias.run.utils.DateUtils;
import com.example.tobias.run.utils.RunUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tobi on 10/20/2017.
 */

public class HistoryPresenter implements Observer<List<Run>>{


    private HistoryView view;
    private List<Run> allRunsList = new ArrayList<>(); //Holds all runs
    private List<Run> displayedRunsList = new ArrayList<>(); //Holds all runs currently displayed in view
    private ObservableDatabase<Run> model;

    public HistoryPresenter(HistoryView view, ObservableDatabase<Run> model){
        this.view = view;
        this.model = model;
        model.attachObserver(this);
    }

    public void onDetachView(){
        //Unsubscribe from model observers list
        model.detachObserver(this);
    }

    public void onStartView(){
        showEmptyViewIfNecessary();
    }

    /**
     * Called from model to to update data
     * @param data
     */
    @Override
    public void updateData(List<Run> data) {
        allRunsList.clear();
        allRunsList.addAll(data);
        updateViewData();
    }

    public void onSpinnerItemSelected(){
        view.finishActionMode();
        updateViewData();
    }

    /**
     * Filters current data according to view's filter and sends it to the view.
     * Then finishes action mode in view and shows the view's empty view if needed.
     */
    public void updateViewData(){
        String filter = view.getDataFilter();
        displayedRunsList.clear();

        switch (filter){
            case "Week" :
                List<Run> weekRuns = RunUtils.filterList(allRunsList,
                        RunPredicates.isRunBetween(DateUtils.getStartOfWeek(), DateUtils.getEndOfWeek()));
                displayedRunsList.addAll(weekRuns);
                break;
            case "Month" :
                List<Run> monthRuns = RunUtils.filterList(allRunsList,
                        RunPredicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth()));
                displayedRunsList.addAll(monthRuns);
                break;
            case "Year" :
                List<Run> yearRuns = RunUtils.filterList(allRunsList,
                        RunPredicates.isRunBetween(DateUtils.getStartOfYear(), DateUtils.getEndOfYear()));
                displayedRunsList.addAll(yearRuns);
                break;
            case "All" :
                displayedRunsList.addAll(allRunsList);
        }

        Collections.sort(displayedRunsList);
        view.setData(displayedRunsList);
        //Exit selection state, user isn't allowed to change filter with selected items.
        view.finishActionMode();

        showEmptyViewIfNecessary();
    }

    private boolean shouldShowEmptyView(){
        return displayedRunsList.isEmpty();
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


    public void deleteRun(List<Integer> indexList){
        for (Integer index : indexList){
            model.remove(displayedRunsList.get(index));
        }
    }

    public void onDeleteMenuClicked(List<Integer> selectedItems){
        view.showDeleteDialog(selectedItems);
    }

    public void onDeleteDialogYes(List<Integer> selectedItems){
        deleteRun(selectedItems);
    }

    public void onEditMenuClicked(List<Integer> selectedItems){
        /*
         * Edit functionality will only be enabled when only one item is selected. Therefore,
         * that item will always be in position 0 of selectedItems.
         */
        int index = selectedItems.get(0);
        Run run = displayedRunsList.get(index);
        //View will handle if run is null.
        view.sendIntentEditorActivity(run);
    }

    private void showEmptyViewIfNecessary(){
        if (shouldShowEmptyView()){
            view.showEmptyView();
        } else {
            view.removeEmptyView();
        }
    }

}
