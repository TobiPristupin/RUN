package com.example.tobias.run.editor;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.Distance;
import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.Run;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.utils.RunUtils;

/**
 * Created by Tobi on 10/23/2017.
 */

public class EditorPresenter {

    private EditorView view;
    private Run runToEdit;
    private boolean editMode;
    private SharedPreferenceRepository preferenceRepository;
    private ObservableDatabase<Run> model;

    public EditorPresenter(EditorView view, SharedPreferenceRepository preferenceRepository, ObservableDatabase<Run> model) {
        this.view = view;
        this.preferenceRepository = preferenceRepository;
        this.model = model;
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
        Distance.Unit unit = preferenceRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);

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
            run = Run.withKilometers(distanceText, timeText, dateText, ratingText);
        } else {
            run = Run.withMiles(distanceText, timeText, dateText, ratingText);
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
            model.update(run);
        } else {
            model.add(run);
        }
    }

}
