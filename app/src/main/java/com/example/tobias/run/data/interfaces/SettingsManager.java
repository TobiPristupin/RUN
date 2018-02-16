package com.example.tobias.run.data.interfaces;

import com.example.tobias.run.data.model.Distance;

/**
 * Created by Tobi on 2/15/2018.
 */

public interface SettingsManager {

    Distance.Unit getDistanceUnit();

    void setDistanceUnit(Distance.Unit unit);
}
