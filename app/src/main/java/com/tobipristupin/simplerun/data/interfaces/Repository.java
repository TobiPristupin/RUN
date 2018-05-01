package com.tobipristupin.simplerun.data.interfaces;


public interface Repository<T> {

    void add(T run);

    void delete(T run);

    void update(T run);
}
