package com.tobipristupin.simplerun.data.manager;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.data.interfaces.PreferencesRepository;
import com.tobipristupin.simplerun.data.model.Distance;

import java.util.Locale;

import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences;

public class SharedPrefRepository implements PreferencesRepository {

    private SharedFirebasePreferences preferences;
    private static final String TAG = "SharedPrefRepository";
    private static final String DISTANCE_UNIT_KEY = "distance_unit";


    public SharedPrefRepository(Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String key = context.getString(R.string.preference_key) + "_" + user.getUid();
        SharedFirebasePreferences.setPathPattern(String.format(Locale.ENGLISH, "users/%s/settings", user.getUid()));

        this.preferences = SharedFirebasePreferences.getInstance(context, key, Context.MODE_PRIVATE);

        preferences.pull().addOnPullCompleteListener(new SharedFirebasePreferences.OnPullCompleteListener() {
            @Override
            public void onPullSucceeded(SharedFirebasePreferences sharedFirebasePreferences) {

            }

            @Override
            public void onPullFailed(Exception e) {
                Log.w(TAG, "pull failed " + e.toString());
                Crashlytics.logException(e);
            }
        });

        if (!isInitialized()){
            initialize();
        }
    }

    @Override
    public Distance.Unit getDistanceUnit() {
        String unit = preferences.getString(DISTANCE_UNIT_KEY, null);
        /*This shouldn't happen, it should be covered by initialization in constructor, but I'm gonna cover it twice
         because I don't trust myself.*/
        if (unit == null){
            //Initialize to default value
            setDistanceUnit(Distance.Unit.MILE);
            return Distance.Unit.MILE;
        }

        return Distance.Unit.get(unit);
    }

    @Override
    public void setDistanceUnit(Distance.Unit unit) {
        //Apply automatically pushes changes to server
        preferences.edit().putString(DISTANCE_UNIT_KEY, unit.toString()).apply();
    }

    private boolean isInitialized(){
        return preferences.getString(DISTANCE_UNIT_KEY, null) != null;
    }

    private void initialize(){
        setDistanceUnit(Distance.Unit.MILE);
    }

}
