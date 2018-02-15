package com.example.tobias.run.data.interfaces;

import com.example.tobias.run.data.model.Run;

public interface Repository {

    void addRun(Run run);

    void deleteRun(Run run);

    void updateRun(Run run);
}
