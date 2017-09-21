package com.example.tobias.run.utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.tobias.run.data.TrackedRun;

import java.util.ArrayList;

/**
 * Created by Tobi on 9/16/2017.
 */

public class DiffCallback extends DiffUtil.Callback {

    private ArrayList<TrackedRun> newList;
    private ArrayList<TrackedRun> oldList;

    public DiffCallback(ArrayList<TrackedRun> newList, ArrayList<TrackedRun> oldList){
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
