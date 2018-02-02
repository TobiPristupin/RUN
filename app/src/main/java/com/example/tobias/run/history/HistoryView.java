package com.example.tobias.run.history;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.Run;

import java.util.List;

/**
 * Created by Tobi on 10/20/2017.
 */

public interface HistoryView {

    void finishActionMode();
    void actionModeSetEditVisible(boolean visible);
    void actionModeSetTitle(String title);
    void actionModeInvalidate();
    void setData(List<Run> data);
    String getDataFilter();

    void showEmptyView();
    void removeEmptyView();
    void showDeleteDialog(List<Integer> selectedItems);
    void sendIntentEditorActivity(@Nullable Run runToEdit);
}
