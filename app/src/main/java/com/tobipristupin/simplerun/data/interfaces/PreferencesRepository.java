package com.tobipristupin.simplerun.data.interfaces;

import com.tobipristupin.simplerun.data.model.Distance;


public interface PreferencesRepository {

    Distance.Unit getDistanceUnit();

    void setDistanceUnit(Distance.Unit unit);
}
