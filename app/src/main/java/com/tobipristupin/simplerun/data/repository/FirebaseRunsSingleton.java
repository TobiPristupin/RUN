package com.tobipristupin.simplerun.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.interfaces.Observable;
import com.tobipristupin.simplerun.interfaces.Observer;
import com.tobipristupin.simplerun.utils.LogWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton class that talks to database and holds all runs. Implements the observable design pattern
 * for real time updating.
 */
public class FirebaseRunsSingleton implements Observable {

    private static final String TAG = "FirebaseRunsSingleton";

    private static FirebaseRunsSingleton instance = new FirebaseRunsSingleton();
    private List<Observer<List<Run>>> observerList = new ArrayList<>();

    private List<Run> cachedRuns = new ArrayList<>();

    private FirebaseRunsSingleton() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference runsDatabaseReferences = firebaseDatabase.getReference("users/" + user.getUid() + "/runs/");

        runsDatabaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseRunsSingleton.this.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseRunsSingleton.this.onDataCancelled(databaseError);
            }

        });
    }

    public static FirebaseRunsSingleton getInstance() {
        return instance;
    }

    /**
     * Resets cache and variables that contain references to database. This method has
     * to be called every time the user starts the app. If it isn't called, variables with references to
     * the previous user's data will remain active.
     */
    public void reset() {
        clearCache();
        instance = new FirebaseRunsSingleton();
   }

   private void clearCache(){
       cachedRuns.clear();
   }

   private void onDataCancelled(DatabaseError error){
       LogWrapper.warn(TAG, "Query error " + error.getMessage());
   }

   private void onDataChange(DataSnapshot snapshot){
       clearCache();
       addRunsToCache(snapshot);
       notifyUpdateObservers();
   }

   private void addRunsToCache(DataSnapshot snapshot){
       for (DataSnapshot data : snapshot.getChildren()){
           Run tr = data.getValue(Run.class);
           cachedRuns.add(tr);
       }
   }

    @Override
    public void attachObserver(Observer observer) {
        if (!observerList.contains(observer)){
            observerList.add(observer);
            if (!cachedRuns.isEmpty()){
                observer.updateData(cachedRuns);
            }
        }
    }

    @Override
    public void detachObserver(Observer o) {
        //remove method returns false if object is not present.
        if (!observerList.remove(o)){
            LogWrapper.info(TAG, "Attempting to remove observer not subscribed");
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
