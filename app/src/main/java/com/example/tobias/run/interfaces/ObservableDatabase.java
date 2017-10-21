package com.example.tobias.run.interfaces;

/**
 * Created by Tobi on 10/20/2017.
 */

public interface ObservableDatabase<T> {
    void attachObserver(Observer o);
    void detachObserver(Observer o);
    void notifyUpdateObservers();
    void add(T data);
    void remove(T data);
    void update(T data);
}
