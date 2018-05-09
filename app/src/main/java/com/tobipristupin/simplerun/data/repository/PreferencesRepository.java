package com.tobipristupin.simplerun.data.repository;

import com.tobipristupin.simplerun.data.model.DistanceUnit;


public interface PreferencesRepository {

    DistanceUnit getDistanceUnit();

    void setDistanceUnit(DistanceUnit unit);
}
