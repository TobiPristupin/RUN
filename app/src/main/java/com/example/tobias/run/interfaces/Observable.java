package com.example.tobias.run.interfaces;

/**
 * Created by Tobi on 10/20/2017.
 */

public interface Observable<T> {
    void attachObserver(Observer o);
    void detachObserver(Observer o);
    void notifyUpdateObservers();
}
