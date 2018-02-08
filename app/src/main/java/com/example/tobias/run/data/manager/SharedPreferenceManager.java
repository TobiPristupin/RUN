package com.example.tobias.run.data.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tobias.run.R;
import com.example.tobias.run.data.interfaces.SharedPreferenceRepository;
import com.example.tobias.run.data.model.Distance;

/**
 * Handles all operations with Shared Preferences. Implements SharedPreferencesRepository.
 */

public class SharedPreferenceManager implements SharedPreferenceRepository {

    public static final String TAG = "SharedPreferenceManager";
    private SharedPreferences sharedPrefs;

    public SharedPreferenceManager(Context context){
        sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_key),
                    Context.MODE_PRIVATE);
    }

    @Override
    public Distance.Unit getDistanceUnit() {
        String value = sharedPrefs.getString(SharedPreferenceRepository.DISTANCE_UNIT_KEY, null);

        if (value == null){
            Log.d(TAG, "No value set for distance unit in shared preferences. Returned null");
            return null;
        }

        return Distance.Unit.get(value);
    }

    @Override
    public void setDistanceUnit(Distance.Unit unit) {
        sharedPrefs.edit().putString(DISTANCE_UNIT_KEY, unit.toString()).apply();
    }
}
