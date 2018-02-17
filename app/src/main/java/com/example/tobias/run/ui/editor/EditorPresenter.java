package com.example.tobias.run.ui.editor;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.interfaces.Repository;
import com.example.tobias.run.data.manager.FirebaseSettingsSingleton;
import com.example.tobias.run.data.model.Distance;
import com.example.tobias.run.data.model.Run;
import com.example.tobias.run.utils.RunUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by Tobi on 10/23/2017.
 */

public class EditorPresenter {

    private EditorView view;
    private Run runToEdit;
    private boolean editMode;
    private Repository repo;

    public EditorPresenter(EditorView view, Repository repo) {
        this.view = view;
        this.repo = repo;
    }

    /**
     * If Run was passed, that means intent for activity was sent with a Run
     * that should be edited. If runToEdit is null, a new Run should be created
     * instead of modifying an existing one.
     * @param runToEdit
     */
    public void onCreateView(@Nullable Run runToEdit){
        if (runToEdit != null) {
            this.runToEdit = runToEdit;
            editMode = true;
            setViewEditMode();
        }
    }

    private void setViewEditMode(){
        Distance.Unit unit = FirebaseSettingsSingleton.getInstance().getDistanceUnit();

        String distanceText = RunUtils.distanceToString(runToEdit.getDistance(unit), unit);
        view.setDistanceText(distanceText);

        String dateText = RunUtils.dateToString(runToEdit.getDate());
        view.setDateText(dateText);

        String timeText = RunUtils.timeToString(runToEdit.getTime(), true);
        view.setTimeText(timeText);

        view.setRatingText(String.valueOf(runToEdit.getRating()));

        view.setSupportActionBarTitle("Edit Run");
    }

    public void onSaveButtonPressed(){
        String distanceText = view.getDistanceText();
        String timeText = view.getTimeText();
        String ratingText = view.getRatingText();
        String dateText = view.getDateText();

        if (!isValid(distanceText, timeText, ratingText, dateText)) {
            view.showInvalidFieldsToast();
            view.vibrate();
            return;
        }

        Run run;

        if (editMode){
            run = putDataIntoExistingRun(distanceText, ratingText, timeText, dateText);
        } else {
            run = createRunFromData(distanceText, ratingText, timeText, dateText);
        }

        addRunToDatabase(run);
        view.showAddedRunSuccessfullyToast();
        view.finishView();
    }

    public void onDistanceDialogPositiveButton(String text){
        view.setDistanceText(text);
    }

    public void onTimeDialogPositiveButton(String text){
        view.setTimeText(text);
    }

    public void onRatingDialogPositiveButton(String text){
        view.setRatingText(text);
    }

    public void onDateDialogPositiveButton(int year, int month, int dayOfMonth){
        DateTimeFormatter formatter;

        if (Locale.getDefault().equals(Locale.US)) {
            formatter = DateTimeFormat.forPattern("E, M/d/y");
        } else {
            formatter = DateTimeFormat.forPattern("E, d/M/y");
        }

        DateTime dateText = new DateTime(year, month + 1, dayOfMonth, 0, 0);

        view.setDateText(formatter.print(dateText));
    }

    private boolean isValid(String... values){
        for(String value : values){
            if (value == null || value.equals("None")){
                return false;
            }
        }
        return true;
    }

    private Run createRunFromData(String distanceText, String ratingText, String timeText, String dateText){
        Run run;
        if (distanceText.contains(Distance.Unit.KM.toString())){
            run = Run.fromKilometers(distanceText, timeText, dateText, ratingText);
        } else {
            run = Run.fromMiles(distanceText, timeText, dateText, ratingText);
        }

        return run;
    }

    private Run putDataIntoExistingRun(String distanceText, String ratingText, String timeText, String dateText){
        runToEdit.setDistance(distanceText);
        runToEdit.setRating(ratingText);
        runToEdit.setTime(timeText);
        runToEdit.setDate(dateText);
        return runToEdit;
    }


    private void addRunToDatabase(Run run){
        if (editMode){
            repo.updateRun(run);
        } else {
            repo.addRun(run);
        }
    }

}
