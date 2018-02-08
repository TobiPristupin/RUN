package com.example.tobias.run.data.manager;

import android.util.Log;

import com.example.tobias.run.data.interfaces.ObservableDatabase;
import com.example.tobias.run.data.model.Run;
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

public class FirebaseDatabaseManager implements ObservableDatabase<Run> {

    private static final String TAG = "FirebaseDatabaseManager";
    private static FirebaseDatabaseManager instance = new FirebaseDatabaseManager();
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseUser user;
    private final DatabaseReference databaseRef;
    private List<Observer<List<Run>>> observerList = new ArrayList<>();
    private List<Run> cachedRuns = new ArrayList<>();

    private FirebaseDatabaseManager(){
        //Private Singleton constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");

        databaseRef.addValueEventListener(new ValueEventListener() {
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
    }

    public static FirebaseDatabaseManager getInstance() {
        return instance;
    }

   public void reset(){
        clearCache();
        instance = new FirebaseDatabaseManager();
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
            //Return an immutable defensive copy of cached runs, to avoid client modifying list
            observer.updateData(Collections.unmodifiableList(cachedRuns));
        }
    }

    @Override
    public void add(Run data) {
        DatabaseReference ref = databaseRef.push();
        data.setId(ref.getKey());
        ref.setValue(data);
    }

    @Override
    public void remove(Run data) {
        databaseRef.child(data.getId()).removeValue();
    }

    @Override
    public void update(Run data) {
        databaseRef.child(data.getId()).setValue(data);
    }
}
