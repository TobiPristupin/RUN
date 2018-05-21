package com.tobipristupin.simplerun.ui.history.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tobipristupin.simplerun.data.RunPredicates;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.data.model.RunFilter;
import com.tobipristupin.simplerun.data.repository.FirebaseRepository;
import com.tobipristupin.simplerun.data.repository.Repository;
import com.tobipristupin.simplerun.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.tobipristupin.simplerun.utils.RunUtils.filterList;

public class HistoryViewModel extends ViewModel {

    private List<Run> allRunsList = new ArrayList<>();
    //TODO: Inject with dagger
    private Repository<Run> repository = new FirebaseRepository();
    private Disposable subscription;
    private RunFilter displayFilter = RunFilter.MONTH;

    private MutableLiveData<List<Run>> displayedRunsList = new MutableLiveData<>(); //Currently displayed runs
    private MutableLiveData<Boolean> displayActionMode = new MutableLiveData<>();


    public HistoryViewModel() {
        subscribeToData();
    }

    @Override
    protected void onCleared() {
        if (subscription != null){
            subscription.dispose();
        }
    }

    private void subscribeToData(){
        subscription = repository.fetch().subscribeWith(new DisposableObserver<List<Run>>(){

            @Override
            public void onNext(List<Run> runs) {
                onNewData(runs);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void onNewData(List<Run> runs){
        allRunsList.clear();
        allRunsList.addAll(runs);
        updateDisplayedRuns();
    }

    private void updateDisplayedRuns(){
        List<Run> filteredRuns = getFilteredRuns();
        Collections.sort(filteredRuns);
        displayedRunsList.setValue(filteredRuns);
        displayActionMode.setValue(false);
    }

    public void setRunFilter(RunFilter filter){
        this.displayFilter = filter;
    }

    public MutableLiveData<List<Run>> getRuns(){
        return displayedRunsList;
    }


    private List<Run> getFilteredRuns(){
        switch (displayFilter){
            case WEEK:
                return filterList(allRunsList,
                        RunPredicates.isRunBetween(DateUtils.getStartOfWeek(), DateUtils.getEndOfWeek()));
            case MONTH:
                return filterList(allRunsList,
                        RunPredicates.isRunBetween(DateUtils.getStartOfMonth(), DateUtils.getEndOfMonth()));
            case YEAR:
                return filterList(allRunsList,
                        RunPredicates.isRunBetween(DateUtils.getStartOfYear(), DateUtils.getEndOfYear()));
            case ALL:
                return allRunsList;
        }

        return allRunsList;
    }
}
