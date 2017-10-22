package com.example.tobias.run.data;

import android.content.Context;

/**
 * Created by Tobi on 10/21/2017.
 */

public interface SharedPreferenceRepository {

    String DISTANCE_UNIT_KEY = "distance_unit";

    String get(String key);
    void set(String key, String value);
}
