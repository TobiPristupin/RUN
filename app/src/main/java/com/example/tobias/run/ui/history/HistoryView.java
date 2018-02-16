package com.example.tobias.run.ui.history;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.model.RunFilter;
import com.example.tobias.run.data.model.Run;

import java.util.List;


public interface HistoryView {

    void finishActionMode();

    void actionModeSetEditVisible(boolean visible);

    void actionModeSetTitle(String title);

    void actionModeInvalidate();

    void setData(List<Run> data);

    RunFilter getDataFilter();

    void setSpinnerSelectedItem(RunFilter filter);

    void showEmptyView();

    void removeEmptyView();

    void showDeleteDialog(List<Integer> selectedItems);

    void sendIntentEditorActivity(@Nullable Run runToEdit);
}
