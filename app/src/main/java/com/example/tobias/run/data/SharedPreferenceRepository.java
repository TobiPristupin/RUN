package com.example.tobias.run.data;

/**
 * Created by Tobi on 10/21/2017.
 */

public interface SharedPreferenceRepository {

    String DISTANCE_UNIT_KEY = "distance_unit";
    String FILTER_KEY = "filter";

    Distance.Unit getDistanceUnit();

    void setDistanceUnit(Distance.Unit unit);

    RunFilter getRunFilter();

    void setRunFilter(RunFilter filter);
}
