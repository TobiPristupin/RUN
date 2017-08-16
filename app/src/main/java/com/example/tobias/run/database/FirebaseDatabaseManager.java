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
        databaseRef.push().setValue(run);
    }

}
