package com.example.tobias.run.editor;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.utils.ConversionUtils;

/**
 * Created by Tobi on 10/23/2017.
 */

public class EditorPresenter {

    private EditorView view;
    private Run runToEdit;
    private boolean editMode;
    private SharedPreferenceRepository preferenceRepository;

    public EditorPresenter(EditorView view, SharedPreferenceRepository preferenceRepository) {
        this.view = view;
        this.preferenceRepository = preferenceRepository;
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
        String unit = preferenceRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);

        String distanceText = ConversionUtils.distanceToString(runToEdit.getDistance(unit), unit);
        view.setDistanceText(distanceText);

        String dateText = ConversionUtils.dateToString(runToEdit.getDate());
        view.setDateText(dateText);

        String timeText = ConversionUtils.timeToString(runToEdit.getTime());
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
        if (distanceText.contains("km")){
            run = Run.withKilometers(dateText, distanceText, timeText, ratingText);
        } else {
            run = Run.withMiles(dateText, distanceText, timeText, ratingText);
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
        ObservableDatabase<Run> database = FirebaseDatabaseManager.getInstance();
        if (editMode){
            database.update(run);
        } else {
            database.add(run);
        }
    }

}
