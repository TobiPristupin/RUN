package com.tobipristupin.simplerun.ui.history.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that implements selection handling in recycler view.
 */

public abstract class SelectableAdapter <VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    /**
     * Indicates the list of selected items
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> itemsIndex = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            itemsIndex.add(selectedItems.keyAt(i));
        }
        return itemsIndex;
    }

    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Count the selected items
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }


}
