package com.example.tobias.run.editor;

/**
 * Created by Tobi on 10/23/2017.
 */

public interface EditorView {

    void showAddedRunSuccessfullyToast();

    void showInvalidFieldsToast();

    void finishView();

    void vibrate();

    void setDistanceText(String text);

    void setDateText(String text);

    void setTimeText(String text);

    void setRatingText(String text);

    String getDistanceText();

    String getDateText();

    String getTimeText();

    String getRatingText();

    void setSupportActionBarTitle(String text);
}
