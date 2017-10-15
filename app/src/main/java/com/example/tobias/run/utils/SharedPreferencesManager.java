package com.example.tobias.run.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tobias.run.R;

/**
 * Created by Tobi on 10/15/2017.
 */

public class SharedPreferencesManager {

    public static String getString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.preference_key)
                , Context.MODE_PRIVATE);

        return sharedPreferences.getString(context.getString(R.string.preference_distance_unit_key), null);
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(context.getResources().getString(R.string.preference_key)
                , Context.MODE_PRIVATE);
    }
}
