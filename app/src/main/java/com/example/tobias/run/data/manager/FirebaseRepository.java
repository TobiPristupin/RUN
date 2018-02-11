package com.example.tobias.run.data.manager;


import com.example.tobias.run.data.interfaces.Repository;
import com.example.tobias.run.data.model.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository implements Repository {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseRef = firebaseDatabase.getReference("users/" + user.getUid() + "/");

    @Override
    public void addRun(Run run) {
        DatabaseReference ref = databaseRef.push();
        run.setId(ref.getKey());
        ref.setValue(run);
    }

    @Override
    public void deleteRun(Run run) {
        databaseRef.child(run.getId()).removeValue();
    }

    @Override
    public void updateRun(Run run) {
        databaseRef.child(run.getId()).setValue(run);
    }

    @Override
    public void setDistanceUnit(String value) {
        databaseRef.child("distance_unit").setValue(value);
    }

    @Override
    public String getDistanceUnit() {
        return null;
    }
}
