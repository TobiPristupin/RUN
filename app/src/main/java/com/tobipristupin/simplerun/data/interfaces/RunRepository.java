package com.tobipristupin.simplerun.data.interfaces;

import com.tobipristupin.simplerun.data.model.Run;

public interface RunRepository {

    void addRun(Run run);

    void deleteRun(Run run);

    void updateRun(Run run);
}
