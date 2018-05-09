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
    private Observable<List<Run>> observable;
    private static final String TAG = "FirebaseRepository";

    private static final FirebaseRepository instance = new FirebaseRepository();

    private FirebaseRepository(){}

    public static FirebaseRepository getInstance(){
        return instance;
    }

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
        return Observable.create(new ObservableOnSubscribe<List<Run>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Run>> emitter) throws Exception {
                runsDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        onNewData(dataSnapshot, emitter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        onDataCancelled(databaseError, emitter);
                    }
                });
            }
        }).replay(1).refCount();
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
