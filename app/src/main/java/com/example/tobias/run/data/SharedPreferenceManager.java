package com.example.tobias.run.data;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.tobias.run.R;
/**
 * Created by Tobi on 10/21/2017.
 */

public class SharedPreferenceManager implements SharedPreferenceRepository {

    private SharedPreferences sharedPrefs;

    public SharedPreferenceManager(Context context){
        sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_key),
                    Context.MODE_PRIVATE);
    }

    @Override
    @Nullable
    public String get(String key) {
        String value = sharedPrefs.getString(key, null);
        return value;
    }

    @Override
    public void set(String key, String value) {
        sharedPrefs.edit().putString(key, value).apply();
    }
}
