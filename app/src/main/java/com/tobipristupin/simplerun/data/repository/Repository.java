package com.tobipristupin.simplerun.data.repository;


import java.util.List;

public interface Repository<T> {

    void add(T run);

    void delete(T run);

    void update(T run);

    io.reactivex.Observable<List<T>> fetch();
}
