package com.example.tobias.run;

import android.support.annotation.Nullable;

import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.history.HistoryPresenter;
import com.example.tobias.run.history.HistoryView;
import com.example.tobias.run.data.ObservableDatabase;
import com.example.tobias.run.interfaces.Observer;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 10/21/2017.
 */

public class HistoryPresenterTest {

    @Test public void updateViewWithDataFromModel(){
        MockView mockView = new MockView();
        MockModel mockModel = new MockModel();

        HistoryPresenter presenter = new HistoryPresenter(mockView, mockModel);
        mockModel.attachObserver(presenter);

        TrackedRun trackedRun = new TrackedRun(10, 10, 10, 10, 1, "a");
        mockModel.add(trackedRun);

        Assert.assertEquals(true, mockView.data.get(0).equals(trackedRun));
    }


    private class MockModel implements ObservableDatabase<TrackedRun> {

        private List<Observer> observers = new ArrayList<>();
        private List<TrackedRun> trackedRunList = new ArrayList<>();

        @Override
        public void attachObserver(Observer o) {
            observers.add(o);
        }

        @Override
        public void detachObserver(Observer o) {
            observers.remove(o);
        }

        @Override
        public void notifyUpdateObservers() {
            for (Observer observer : observers){
                observer.updateData(trackedRunList);
            }
        }

        @Override
        public void add(TrackedRun data) {
            trackedRunList.add(data);
            notifyUpdateObservers();
        }

        @Override
        public void remove(TrackedRun data) {
        }

        @Override
        public void update(TrackedRun data) {
        }
    }


    private class MockView implements HistoryView {

        public List<TrackedRun> data = new ArrayList<>();

        @Override
        public void setData(List<TrackedRun> data) {
            this.data = data;
        }

        @Override
        public boolean adapterIsDataSetEmpty() {
            return false;
        }

        @Override
        public String getDataFilter() {
            return "All";
        }


        @Override
        public void finishActionMode() {
        }
        @Override
        public void actionSetEditVisible(boolean visible) {

        }
        @Override
        public void actionModeSetTitle(String title) {

        }
        @Override
        public void actionModeInvalidate() {

        }
        @Override
        public void showEmptyView(boolean longMessage) {

        }
        @Override
        public void removeEmptyView() {

        }
        @Override
        public void showDeleteDialog(List<Integer> selectedItems) {

        }
        @Override
        public void sendIntentEditorActivity(@Nullable TrackedRun runToEdit) {

        }
    }
}
