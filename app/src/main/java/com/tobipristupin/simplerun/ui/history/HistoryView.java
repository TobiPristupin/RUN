package com.tobipristupin.simplerun.ui.history;

import android.support.annotation.Nullable;

import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.data.model.RunFilter;

import java.util.List;


public interface HistoryView {

    void finishActionMode();

    void actionModeSetEditVisible(boolean visible);

    void actionModeSetTitle(String title);

    void actionModeInvalidate();

    void setData(List<Run> data);

    RunFilter getDataFilter();

    void showEmptyView();

    void removeEmptyView();

    void showDeleteDialog(List<Integer> selectedItems);

    void sendIntentEditorActivity(@Nullable Run runToEdit);
}
