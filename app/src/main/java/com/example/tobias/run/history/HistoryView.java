package com.example.tobias.run.history;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.TrackedRun;

import java.util.List;

/**
 * Created by Tobi on 10/20/2017.
 */

public interface HistoryView {

    void finishActionMode();
    void actionModeSetEditVisible(boolean visible);
    void actionModeSetTitle(String title);
    void actionModeInvalidate();
    void setData(List<TrackedRun> data);
    boolean adapterIsDataSetEmpty();
    String getDataFilter();
    void showEmptyView(boolean longMessage);
    void removeEmptyView();
    void showDeleteDialog(List<Integer> selectedItems);
    void sendIntentEditorActivity(@Nullable TrackedRun runToEdit);
}
