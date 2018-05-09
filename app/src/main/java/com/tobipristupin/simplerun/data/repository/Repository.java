package com.tobipristupin.simplerun.data.repository;


import com.tobipristupin.simplerun.interfaces.Observable;

import java.util.List;

public interface Repository<T> {

    void add(T run);

    void delete(T run);

    void update(T run);

    io.reactivex.Observable<List<T>> fetch();
}
