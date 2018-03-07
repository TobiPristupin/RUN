package com.tobipristupin.simplerun.data.manager;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.data.interfaces.PreferencesRepository;
import com.tobipristupin.simplerun.data.model.DistanceUnit;

import java.util.Locale;

import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences;

public class SharedPrefRepository implements PreferencesRepository {

    private SharedFirebasePreferences preferences;
    private static final String TAG = "SharedPrefRepository";
    private static final String DISTANCE_UNIT_KEY = "distance_unit";
    private Context context;

    public SharedPrefRepository(Context context) {
        this.context = context;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String key = getPreferencesKeyFromUser(user);

        /*setDatabasePath has to be called before getting the instance of SharedFirebasePreferences
        * for the new path to be applied. This order dependency / bug comes is from the library not from the
        * app*/
        setDatabasePath(user);

        this.preferences = SharedFirebasePreferences.getInstance(context, key, Context.MODE_PRIVATE);


        pullFromDatabase();

        if (!isInitialized()){
            initialize();
        }
    }

    @Override
    public DistanceUnit getDistanceUnit() {
        String unit = preferences.getString(DISTANCE_UNIT_KEY, null);
        /*This shouldn't happen, it should be covered by initialization in constructor, but I'm gonna cover it twice
         because I don't trust myself.*/
        if (unit == null){
            //Initialize to default value
            setDistanceUnit(DistanceUnit.MILE);
            return DistanceUnit.MILE;
        }

        return DistanceUnit.fromString(unit);
    }

    @Override
    public void setDistanceUnit(DistanceUnit unit) {
        //Apply automatically pushes changes to server
        preferences.edit().putString(DISTANCE_UNIT_KEY, unit.toString()).apply();
    }

    private void pullFromDatabase(){
        preferences.pull().addOnPullCompleteListener(new SharedFirebasePreferences.OnPullCompleteListener() {
            @Override
            public void onPullSucceeded(SharedFirebasePreferences sharedFirebasePreferences) {

            }

            @Override
            public void onPullFailed(Exception e) {
                SharedPrefRepository.this.onPullFailed(e);
            }
        });
    }

    private void onPullFailed(Exception e){
        Log.w(TAG, "pull failed " + e.toString());
        Crashlytics.logException(e);
    }

    private boolean isInitialized(){
        return preferences.getString(DISTANCE_UNIT_KEY, null) != null;
    }

    private void initialize(){
        setDistanceUnit(DistanceUnit.MILE);
    }

    /**
     * Each user has a shared preferences file for themselves. This method returns the name
     * of that file given the user.
     * @param user
     * @return
     */
    private String getPreferencesKeyFromUser(FirebaseUser user){
        return context.getString(R.string.preference_key) + "_" + user.getUid();
    }

    /**
     * Sets the database path the firebase shared preferences will be reading and writing from
     * @param user
     */
    private void setDatabasePath(FirebaseUser user){
        SharedFirebasePreferences.setPathPattern(String.format(Locale.ENGLISH, "users/%s/settings", user.getUid()));
    }

}
