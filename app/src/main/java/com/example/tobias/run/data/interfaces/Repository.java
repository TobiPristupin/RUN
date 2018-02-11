package com.example.tobias.run.data.interfaces;

import com.example.tobias.run.data.model.Run;

/**
 * Created by Tobi on 2/11/2018.
 */

public interface Repository {

    void addRun(Run run);

    void deleteRun(Run run);

    void updateRun(Run run);

    String getDistanceUnit();

    void setDistanceUnit(String value);
}
