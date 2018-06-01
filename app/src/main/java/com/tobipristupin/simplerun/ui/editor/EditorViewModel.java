package com.tobipristupin.simplerun.ui.editor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.google.android.gms.common.util.Strings;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.data.model.DistanceUnit;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.data.repository.PreferencesRepository;
import com.tobipristupin.simplerun.data.repository.Repository;
import com.tobipristupin.simplerun.utils.DateUtils;
import com.tobipristupin.simplerun.utils.SingleLiveEvent;
import com.tobipristupin.simplerun.utils.VoidSingleLiveEvent;

import org.joda.time.DateTime;

import java.text.DecimalFormat;

import static com.tobipristupin.simplerun.utils.RunUtils.*;

public class EditorViewModel extends ViewModel {

    private Run runToEdit;
    private boolean editMode;
    private String emptyFieldText;
    private Repository<Run> runRepo;
    private PreferencesRepository preferencesRepo;

    private MutableLiveData<String> distance = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();
    private MutableLiveData<String> time = new MutableLiveData<>();
    private MutableLiveData<String> rating = new MutableLiveData<>();
    private MutableLiveData<Integer> actionBarTitle = new MutableLiveData<>();
    private SingleLiveEvent<Integer> showErrorToast = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> showSuccessToast = new SingleLiveEvent<>();
    private VoidSingleLiveEvent vibrate = new VoidSingleLiveEvent();
    private VoidSingleLiveEvent finishView = new VoidSingleLiveEvent();

    private EditorViewModel(PreferencesRepository preferencesRepo, Repository<Run> runRepo, String emptyFieldText){
        this.preferencesRepo = preferencesRepo;
        this.runRepo = runRepo;
        this.emptyFieldText = emptyFieldText;

        setDefaults();
    }

    private void setDefaults(){
        distance.postValue(emptyFieldText);
        date.postValue(emptyFieldText);
        time.postValue(emptyFieldText);
        rating.postValue(emptyFieldText);
    }

    public void setRunToEdit(Run run){
        this.runToEdit = run;
        editMode = true;
        initializeEditMode();
    }

    public void onSaveButtonPressed(){
        if (!areValid(date.getValue(), distance.getValue(), time.getValue(), rating.getValue())){
            invalidInput();
            return;
        }

        if (editMode){
            updateRun();
        } else {
            addNewRun();
        }
    }

    public void onHomePressed(){
        finishView.call();
    }

    public void onDistanceDialogFinish(int wholeNum, int fractionalNum){
        String distance = "" + wholeNum + "." + fractionalNum + " " +
                preferencesRepo.getDistanceUnit().toString();

        this.distance.postValue(distance);
    }

    public void onTimeDialogFinish(int hours, int minutes, int seconds){
        DecimalFormat df = new DecimalFormat("00");
        String time = df.format(hours) + ":"
                + df.format(minutes) + ":"
                + df.format(seconds);
        this.time.postValue(time);
    }

    public void onRatingDialogFinish(int rating){
        this.rating.postValue(String.valueOf(rating));
    }

    /**
     * @param month range 1-12
     * @param dayOfMonth range 1-31
     */
    public void onDateDialogFinish(int year, int month, int dayOfMonth){
        DateTime date = new DateTime(year, month, dayOfMonth, 0, 0);
        this.date.postValue(DateUtils.datetimeToString(date));
    }

    private void updateRun() {
        runToEdit.setDistance(distance.getValue());
        runToEdit.setRating(rating.getValue());
        runToEdit.setDate(date.getValue());
        runToEdit.setTime(time.getValue());
        runRepo.update(runToEdit);
        notifyRunAdded();
    }

    private void addNewRun() {
        Run run;
        if (distance.getValue().contains(DistanceUnit.KM.toString())){
            run = Run.fromKilometers(distance.getValue(), time.getValue(), date.getValue(), rating.getValue());
        } else {
            run = Run.fromMiles(distance.getValue(), time.getValue(), date.getValue(), rating.getValue());
        }

        runRepo.add(run);
        notifyRunAdded();
    }

    private void notifyRunAdded(){
        showSuccessToast.postValue(R.string.editor_added);
        finishView.call();
    }

    private void invalidInput() {
        showErrorToast.postValue(R.string.editor_fillin_fields);
        vibrate.call();
    }

    private void initializeEditMode(){
        DistanceUnit unit = preferencesRepo.getDistanceUnit();
        distance.postValue(distanceToString(runToEdit.getDistance(unit), unit));
        date.postValue(dateToString(runToEdit.getDate()));
        time.postValue(timeToString(runToEdit.getTime(), true));
        rating.postValue(ratingToString(runToEdit.getRating()));
        actionBarTitle.postValue(R.string.editor_toolbar_edit);
    }

    private boolean areValid(String... fields){
        for (String value : fields){
            if (Strings.isEmptyOrWhitespace(value) || value.equals(emptyFieldText)){
                return false;
            }
        }

        return true;
    }

    public LiveData<Integer> getActionBarTitle() {
        return actionBarTitle;
    }

    public SingleLiveEvent<Integer> getShowErrorToast() {
        return showErrorToast;
    }

    public SingleLiveEvent<Integer> getShowSuccessToast() {
        return showSuccessToast;
    }

    public VoidSingleLiveEvent getVibrate() {
        return vibrate;
    }

    public VoidSingleLiveEvent getFinishView() {
        return finishView;
    }

    public MutableLiveData<String> getDistance() {
        return distance;
    }

    public MutableLiveData<String> getDate() {
        return date;
    }

    public MutableLiveData<String> getTime() {
        return time;
    }

    public MutableLiveData<String> getRating() {
        return rating;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private PreferencesRepository preferencesRepository;
        private Repository<Run> runRepository;
        private String emptyFieldText;

        public Factory(PreferencesRepository preferencesRepository, Repository<Run> runRepository, String emptyFieldText) {
            this.preferencesRepository = preferencesRepository;
            this.runRepository = runRepository;
            this.emptyFieldText = emptyFieldText;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(EditorViewModel.class)){
                return (T) new EditorViewModel(preferencesRepository, runRepository, emptyFieldText);
            }

            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
