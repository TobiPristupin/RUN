package com.example.tobias.run.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Tobi on 8/14/2017.
 */

public class FirebaseDatabaseManager {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid());


    public void addRun(TrackedRun run){
        DatabaseReference ref = databaseRef.push();
        run.setId(ref.getKey());
        System.out.println(ref.getKey());
        ref.setValue(run);
    }

    public void deleteRun(TrackedRun run){
        databaseRef.child(run.getId()).removeValue();
    }

    public void updateRun(TrackedRun run){
        databaseRef.child(run.getId()).setValue(run);
    }

}
