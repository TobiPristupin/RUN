package com.example.tobias.run.interfaces;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface Observable {
    void attachObserver(Observer o);
    void detachObserver(Observer o);
    void notifyUpdateObservers();
}
