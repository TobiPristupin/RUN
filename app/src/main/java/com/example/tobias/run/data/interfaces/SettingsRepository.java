package com.example.tobias.run.data.interfaces;

import com.example.tobias.run.data.model.Distance;

public interface SettingsRepository {

    Distance.Unit getDistanceUnit();

    void setDistanceUnit(Distance.Unit unit);
}
