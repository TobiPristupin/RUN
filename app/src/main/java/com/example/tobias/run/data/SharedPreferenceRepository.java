package com.example.tobias.run.data;

/**
 * Created by Tobi on 10/21/2017.
 */

public interface SharedPreferenceRepository {

    String DISTANCE_UNIT_KEY = "distance_unit";

    Distance.Unit get(String key);
    void set(String key, String value);
}
