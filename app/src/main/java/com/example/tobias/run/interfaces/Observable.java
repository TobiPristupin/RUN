package com.example.tobias.run.interfaces;


public interface Observable {

    void attachObserver(Observer o);

    void detachObserver(Observer o);

    void notifyUpdateObservers();
}
