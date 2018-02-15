package com.example.tobias.run.data.manager;

import android.util.Log;

import com.example.tobias.run.data.model.Distance;
import com.example.tobias.run.data.model.Run;
import com.example.tobias.run.interfaces.Observable;
import com.example.tobias.run.interfaces.Observer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseDataSingleton implements Observable {

    private static final String TAG = "FirebaseDataSingleton";
    private static final String DISTANCE_UNIT_KEY = "distance_unit";

    private static FirebaseDataSingleton instance = new FirebaseDataSingleton();
    private List<Observer<List<Run>>> observerList = new ArrayList<>();

    private List<Run> cachedRuns = new ArrayList<>();
    private Distance.Unit distanceUnit;

    private FirebaseDataSingleton() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        DatabaseReference runsDatabaseReferences = firebaseDatabase.getReference("users/" + user.getUid() + "/runs/");
        DatabaseReference settingsDatabaseReferences = firebaseDatabase.getReference("users/" + user.getUid() + "/settings/");

        runsDatabaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cachedRuns.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Run tr = data.getValue(Run.class);
                    cachedRuns.add(tr);
                }
                notifyUpdateObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Query error " + databaseError.getMessage());
            }

        });

        settingsDatabaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch (dataSnapshot.getKey()) {
                    case DISTANCE_UNIT_KEY:
                        distanceUnit = Distance.Unit.get((String) dataSnapshot.getValue());
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Query error " + databaseError.getMessage());
            }
        });
    }

    public static FirebaseDataSingleton getInstance() {
        return instance;
    }

    /**
     * Resets cache and variables that contain references to database. This method has
     * to be called every time the user is logged out. If it isn't called, variables with references to
     * the previous user's data will remain active.
     */
    public void reset() {
        clearCache();
       instance = new FirebaseDataSingleton();
   }

   private void clearCache(){
       cachedRuns.clear();
   }

    @Override
    public void attachObserver(Observer o) {
        if (!observerList.contains(o)){
            observerList.add(o);
            if (!cachedRuns.isEmpty()){
                o.updateData(cachedRuns);
            }
        }
    }

    @Override
    public void detachObserver(Observer o) {
        //remove method returns false if object is not present.
        if (!observerList.remove(o)){
            Log.d(TAG, "Attempting to remove observer not subscribed");
        }
    }

    @Override
    public void notifyUpdateObservers() {
        for (Observer<List<Run>> observer : observerList){
            //Return an immutable defensive copy of cached runs, to avoid client modifying reference to list
            observer.updateData(Collections.unmodifiableList(cachedRuns));
        }
    }
}
