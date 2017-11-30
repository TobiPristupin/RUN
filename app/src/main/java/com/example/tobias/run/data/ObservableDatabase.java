package com.example.tobias.run.data;

import com.example.tobias.run.interfaces.Observable;

/**
 * Created by Tobi on 10/20/2017.
 */

public interface ObservableDatabase<T> extends Observable {

    void add(T data);

    void remove(T data);

    void update(T data);
}
