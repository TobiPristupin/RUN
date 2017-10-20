package com.example.tobias.run;

import com.example.tobias.run.interfaces.Observable;
import com.example.tobias.run.interfaces.Observer;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 10/20/2017.
 */

public class ObserverTest {

    @Test public void observerShouldGetData() {
        Observer observer = new MockObserver("Hello");
        Observable observable = new MockObservable("Hello");

        observable.attachObserver(observer);
        observable.notifyUpdateObservers();

        Assert.assertEquals(true, ((MockObserver)observer).updateCalledWithData);
    }

    private class MockObserver implements Observer<String> {

        public boolean updateCalledWithData = false;
        private String string;

        public MockObserver(String data){
            this.string = data;
        }

        @Override
        public void update(List<String> data) {
            if (string.equals(data.get(0))){
                updateCalledWithData = true;
            }
        }
    }

    private class MockObservable implements Observable<String> {

        private List<Observer> observers = new ArrayList<>();
        private List<String> data = new ArrayList<>();

        public MockObservable(String data){
            this.data.add(data);
        }

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
                observer.update(data);
            }
        }
    }
}
