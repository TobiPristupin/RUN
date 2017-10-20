package com.example.tobias.run.data;

/**
 * Created by Tobi on 10/20/2017.
 */

public interface Database<T> {
    void add(T data);
    void remove(T data);
    void update(T data);
}
