package com.example.tobias.run.editor;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.utils.ConversionUtils;

import java.util.HashMap;

/**
 * Created by Tobi on 10/23/2017.
 */

public class EditorPresenter {

    private EditorView view;
    private TrackedRun trackedRun;
    private SharedPreferenceRepository preferenceRepository;
    private boolean isInEditMode = false;

    public EditorPresenter(EditorView view, SharedPreferenceRepository preferenceRepository) {
        this.view = view;
        this.preferenceRepository = preferenceRepository;
    }

    /**
     * If TrackedRun was passed, that means intent for activity was sent with a TrackedRun
     * that should be edited. If trackedRunToEdit is null, a new TrackedRun should be created
     * instead of modifying an existing one.
     * @param trackedRunToEdit
     */
    public void onCreateView(@Nullable TrackedRun trackedRunToEdit){
        if (trackedRunToEdit != null){
            trackedRun = trackedRunToEdit;
            view.setEditMode(trackedRun);
            isInEditMode = true;
        } else {
            trackedRun = new TrackedRun();
        }
    }

    public void onSaveButtonPressed(){
        HashMap<String, String> data = view.retrieveDataFromViews();
        if (isValid(data)){
            setDataIntoTrackedRun(data);
            addRunToDatabase();
            view.showAddedRunSuccessfullyToast();
            view.finishView();
        } else {
            view.showInvalidFieldsToast();
            view.vibrate();
        }
    }

    private boolean isValid(HashMap<String, String> data){
        for(String value : data.values()){
            if (value == null || value.equals("None")){
                return false;
            }
        }
        return true;
    }

    private void setDataIntoTrackedRun(HashMap<String, String> data){
        if (preferenceRepository.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY).equals("km")){
            float kmDistance = ConversionUtils.distanceToFloat(data.get("distance"));
            trackedRun.setDistanceKilometres(kmDistance);
            trackedRun.setDistanceMiles(ConversionUtils.kilometresToMiles(kmDistance));
        } else {
            float miDistance = ConversionUtils.distanceToFloat(data.get("distance"));
            trackedRun.setDistanceMiles(miDistance);
            trackedRun.setDistanceKilometres(ConversionUtils.milesToKilometers(miDistance));
        }

        trackedRun.setDate(ConversionUtils.dateToUnix(data.get("date")));
        trackedRun.setRating(Integer.valueOf(data.get("rating")));
        trackedRun.setTime(ConversionUtils.timeToUnix(data.get("time")));
        trackedRun.setMilePace(ConversionUtils.calculatePace(trackedRun.getDistanceMiles(), trackedRun.getTime()));
        trackedRun.setKmPace(ConversionUtils.calculatePace(trackedRun.getDistanceKilometres(), trackedRun.getTime()));
    }

    private void addRunToDatabase(){
        ObservableDatabase<TrackedRun> database = FirebaseDatabaseManager.getInstance();
        if (isInEditMode){
            database.update(trackedRun);
        } else {
            database.add(trackedRun);
        }
    }

    //TODO: refactor https://softwareengineering.stackexchange.com/questions/312520/in-mvp-pattern-should-the-view-instantiate-a-model-object-based-on-ui-contents


}
