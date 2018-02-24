package com.tobipristupin.simplerun.data.interfaces;

import com.tobipristupin.simplerun.data.model.DistanceUnit;

public interface SettingsRepository {

    DistanceUnit getDistanceUnit();

    void setDistanceUnit(DistanceUnit unit);
}
