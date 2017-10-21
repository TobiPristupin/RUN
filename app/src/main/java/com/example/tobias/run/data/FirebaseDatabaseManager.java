package com.example.tobias.run.data;

import android.util.Log;

import com.example.tobias.run.interfaces.ObservableDatabase;
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

public class FirebaseDatabaseManager implements ObservableDatabase<TrackedRun> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");
    private List<Observer<TrackedRun>> observerList = new ArrayList<>();
    private List<TrackedRun> trackedRunList = new ArrayList<>();
    private static final String TAG = "FirebaseDatabaseManager";

    public FirebaseDatabaseManager(){
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackedRunList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    TrackedRun tr = data.getValue(TrackedRun.class);
                    trackedRunList.add(tr);
                }
                notifyUpdateObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    @Override
    public void attachObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void detachObserver(Observer o) {
        //remove method returns false if object is not present.
        if (!observerList.remove(o)){
            Log.d(TAG, "Attempting to remove not subscribed observer");
        }
    }

    @Override
    public void notifyUpdateObservers() {
        for (Observer<TrackedRun> observer : observerList){
            observer.updateData(trackedRunList);
        }
    }

    @Override
    public void add(TrackedRun data) {
        DatabaseReference ref = databaseRef.push();
        data.setId(ref.getKey());
        ref.setValue(data);
    }

    @Override
    public void remove(TrackedRun data) {
        databaseRef.child(data.getId()).removeValue();
    }

    @Override
    public void update(TrackedRun data) {
        databaseRef.child(data.getId()).setValue(data);
    }
}
