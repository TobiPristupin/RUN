package com.tobipristupin.simplerun.data.repository;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tobipristupin.simplerun.data.model.Run;
import com.tobipristupin.simplerun.utils.LogWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class FirebaseRepository implements Repository<Run> {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private DatabaseReference runsDatabaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/runs/");
    private static Observable<List<Run>> observable;

    private static final String TAG = "FirebaseRepository";

    @Override
    public void add(Run run) {
        DatabaseReference ref = runsDatabaseRef.push();
        run.setId(ref.getKey());
        ref.setValue(run);
    }

    @Override
    public void delete(Run run) {
        runsDatabaseRef.child(run.getId()).removeValue();
    }

    @Override
    public void update(Run run) {
        runsDatabaseRef.child(run.getId()).setValue(run);
    }

    @Override
    public Observable<List<Run>> fetch() {
        if (observable == null){
            observable = createObservable();
        }

        return observable;
    }

    private Observable<List<Run>> createObservable(){
        return Observable.create((ObservableOnSubscribe<List<Run>>) emitter -> {
            ValueEventListener databaseListener = createDatabaseConnection(emitter);
            runsDatabaseRef.addValueEventListener(databaseListener);
            emitter.setCancellable(() -> runsDatabaseRef.removeEventListener(databaseListener));
        }).replay(1).refCount();
    }

    private ValueEventListener createDatabaseConnection(ObservableEmitter<List<Run>> emitter){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onNewData(dataSnapshot, emitter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onDataCancelled(databaseError, emitter);
            }
        };
    }

    private void onNewData(DataSnapshot snapshot, ObservableEmitter<List<Run>> emitter){
        List<Run> runs = new ArrayList<>();

        for (DataSnapshot data : snapshot.getChildren()){
            Run tr = data.getValue(Run.class);
            runs.add(tr);
        }

        emitter.onNext(runs);
    }

    private void onDataCancelled(DatabaseError error, ObservableEmitter<List<Run>> emitter){
        LogWrapper.warn(TAG, "Query error " + error.getMessage());
        emitter.onError(error.toException());
    }
}
