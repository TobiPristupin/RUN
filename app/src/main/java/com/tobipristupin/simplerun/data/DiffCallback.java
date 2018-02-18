package com.tobipristupin.simplerun.data;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.tobipristupin.simplerun.data.model.Run;

import java.util.List;

/**
 * Uses Diff algorithm to calculate necessary updates to recycler view items when modifying data set.
 */

public class DiffCallback extends DiffUtil.Callback {

    private List<Run> newList;
    private List<Run> oldList;

    public DiffCallback(List<Run> newList, List<Run> oldList){
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
