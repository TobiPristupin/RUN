package com.example.tobias.run.app;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.tobias.run.data.Distance;
import com.example.tobias.run.data.FirebaseDatabaseManager;
import com.example.tobias.run.data.RunFilter;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainPresenter {

    private MainView view;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;
    private SharedPreferenceRepository preferenceRepository;

    public MainPresenter(MainView view, SharedPreferenceRepository preferenceRepository){
        this.view = view;
        this.preferenceRepository = preferenceRepository;
        user = firebaseAuth.getCurrentUser();
    }

    public void onCreateView(){
        authenticate();
    }


    public void authenticate(){
        initAuthStateListener();

        if (!userLoggedIn()){
            view.loadLogIn();
        } else {
            view.initViews();
            FirebaseDatabaseManager.getInstance().reset();
        }
    }

    public void onResumeView(){
        firebaseAuth.addAuthStateListener(authListener);
    }

    public void onPauseView(){
        firebaseAuth.removeAuthStateListener(authListener);
    }

    private boolean userLoggedIn(){
        return user != null;
    }

    private void initAuthStateListener(){
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null){
                    view.loadLogIn();
                }
            }
        };
    }

    /**
     * If shared preferences distance unit hasn't been initialized (user reset app or first time using it)
     * sets it to default value (mi)
     */
    public void initSharedPreferences(){
        //If values haven't been set, add default values
        if (preferenceRepository.getDistanceUnit() == null) {
            preferenceRepository.setDistanceUnit(Distance.Unit.KM);
        }

        if (preferenceRepository.getRunFilter() == null) {
            preferenceRepository.setRunFilter(RunFilter.MONTH);
        }
    }

    public String getUserDisplayName(){
        return user.getDisplayName();
    }

    public String getUserEmail(){
        return user.getEmail();
    }

    public Uri getUserPhotoUrl(){
        return user.getPhotoUrl();
    }

}
