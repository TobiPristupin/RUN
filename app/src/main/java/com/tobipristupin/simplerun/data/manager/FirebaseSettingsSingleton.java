//package com.tobipristupin.simplerun.data.manager;
//
//import android.util.Log;
//
//import com.tobipristupin.simplerun.data.interfaces.SettingsRepository;
//import com.tobipristupin.simplerun.data.model.Distance;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//
///**
// * Singleton that stores all user settings from database
// */
//
//public class FirebaseSettingsSingleton implements SettingsRepository {
//
//    public static final String DISTANCE_UNIT_KEY = "distance_unit";
//    //Tag shortened because Log tag has to be shorter than 24 chars.
//    public static final String TAG = "FirebaseSettingsSnglton";
//    private static FirebaseSettingsSingleton instance = new FirebaseSettingsSingleton();
//    private Distance.Unit distanceUnit;
//
//    private FirebaseSettingsSingleton(){
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        DatabaseReference databaseReference = firebaseDatabase.getReference("users/" + user.getUid() + "/settings/");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    switch (snapshot.getKey()){
//                        case DISTANCE_UNIT_KEY :
//                            distanceUnit = Distance.Unit.get(snapshot.getValue().toString());
//                            break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "Error retrieving settings data " + databaseError.toString());
//            }
//        });
//    }
//
//    public static FirebaseSettingsSingleton getInstance(){
//        return instance;
//    }
//
//    public void reset(){
//        instance = new FirebaseSettingsSingleton();
//    }
//
//    public void initializeSettings(){
//        if (distanceUnit == null){
//            distanceUnit = Distance.Unit.MILE;
//        }
//    }
//
//    @Override
//    public Distance.Unit getDistanceUnit() {
//        if (distanceUnit == null) {
//            throw new RuntimeException("Settings have not been initialized");
//        }
//
//        return distanceUnit;
//    }
//
//    @Override
//    public void setDistanceUnit(Distance.Unit unit) {
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        DatabaseReference reference = firebaseDatabase.getReference("users/" + user.getUid() + "/settings/");
//        reference.child(DISTANCE_UNIT_KEY).setValue(unit.toString());
//        distanceUnit = unit;
//    }
//}
