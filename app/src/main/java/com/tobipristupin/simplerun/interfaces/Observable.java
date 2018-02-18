package com.tobipristupin.simplerun.interfaces;


public interface Observable {

    void attachObserver(Observer o);

    void detachObserver(Observer o);

    void notifyUpdateObservers();
}
