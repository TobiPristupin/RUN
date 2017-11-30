package com.example.tobias.run.data;

import android.util.Log;

import com.example.tobias.run.interfaces.Observer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseManager implements ObservableDatabase<Run> {

    private List<Observer<List<Run>>> observerList = new ArrayList<>();
    private List<Run> cachedRuns = new ArrayList<>();
    private static final String TAG = "FirebaseDatabaseManager";

    private static FirebaseDatabaseManager instance = new FirebaseDatabaseManager();

    private FirebaseDatabaseManager(){
        //Private Singleton constructor
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");

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

    @Override
    public void attachObserver(Observer o) {
        if (!observerList.contains(o)){
            observerList.add(o);
            o.updateData(cachedRuns);
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
            observer.updateData(cachedRuns);
        }
    }

    @Override
    public void add(Run data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");

        DatabaseReference ref = databaseRef.push();
        data.setId(ref.getKey());
        ref.setValue(data);
    }

    @Override
    public void remove(Run data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");

        databaseRef.child(data.getId()).removeValue();
    }

    @Override
    public void update(Run data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");

        databaseRef.child(data.getId()).setValue(data);
    }
}
