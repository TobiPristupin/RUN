package com.tobipristupin.simplerun.ui.editor;

/**
 * Created by Tobi on 10/23/2017.
 */

public interface EditorView {

    void showAddedRunSuccessfullyToast();

    void showInvalidFieldsToast();

    void finishView();

    void vibrate();

    String getDistanceText();

    void setDistanceText(String text);

    String getDateText();

    void setDateText(String text);

    String getTimeText();

    void setTimeText(String text);

    String getRatingText();

    void setRatingText(String text);

    void setActionBarEditTitle();

    /**
     * @return text used to denote an empty field
     */
    String getEmptyFieldText();
}
