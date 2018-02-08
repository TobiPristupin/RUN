package com.example.tobias.run.data.interfaces;


import com.example.tobias.run.data.model.Distance;

public interface SharedPreferenceRepository {

    String DISTANCE_UNIT_KEY = "distance_unit";

    Distance.Unit getDistanceUnit();

    void setDistanceUnit(Distance.Unit unit);

}
