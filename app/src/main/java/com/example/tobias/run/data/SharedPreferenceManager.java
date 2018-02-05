package com.example.tobias.run.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tobias.run.R;

/**
 * Created by Tobi on 10/21/2017.
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
    public RunFilter getRunFilter() {
        String value = sharedPrefs.getString(SharedPreferenceRepository.FILTER_KEY, null);

        if (value == null) {
            Log.d(TAG, "No value set for distance unit in shared preferences. Returned null");
            return null;
        }

        return RunFilter.get(value);
    }

    @Override
    public void setRunFilter(RunFilter filter) {
        sharedPrefs.edit().putString(FILTER_KEY, filter.toString()).apply();
    }

    @Override
    public void setDistanceUnit(Distance.Unit unit) {
        sharedPrefs.edit().putString(DISTANCE_UNIT_KEY, unit.toString()).apply();
    }
}
