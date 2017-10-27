package com.example.tobias.run.editor;

import com.example.tobias.run.data.TrackedRun;

import java.util.HashMap;

/**
 * Created by Tobi on 10/23/2017.
 */

public interface EditorView {

    void setEditMode(TrackedRun tr);

    HashMap<String, String> retrieveDataFromViews();

    void showAddedRunSuccessfullyToast();

    void showInvalidFieldsToast();

    void finishView();

    void vibrate();
}
