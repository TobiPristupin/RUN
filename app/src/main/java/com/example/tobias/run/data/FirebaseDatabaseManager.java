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

public class FirebaseDatabaseManager implements ObservableDatabase<TrackedRun> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");
    private List<Observer<List<TrackedRun>>> observerList = new ArrayList<>();
    private List<TrackedRun> trackedRunList = new ArrayList<>();
    private static final String TAG = "FirebaseDatabaseManager";

    private static FirebaseDatabaseManager instance = new FirebaseDatabaseManager();

    private FirebaseDatabaseManager(){
        //Private Singleton constructor
    }

    public static FirebaseDatabaseManager getInstance() {
        return instance;
    }

    /**
     * Add listener to online database. When this method isn't called, observable functionality cannot be used
     * because there won't be data to send. Database functionality may still be used without a query listener.
     */
    @Override
    public void startQuery(){
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
                Log.w(TAG, "Query error " + databaseError.getMessage());
            }

        });
    }


    @Override
    public void attachObserver(Observer o) {
        if (!observerList.contains(o)){
            observerList.add(o);
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
        for (Observer<List<TrackedRun>> observer : observerList){
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
