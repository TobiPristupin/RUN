package com.example.tobias.run.ui.main;

import android.support.annotation.NonNull;

import com.example.tobias.run.data.interfaces.Repository;
import com.example.tobias.run.data.model.Distance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Presenter for MainView implementation
 */

public class MainPresenter {

    private MainView view;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authListener;
    private Repository repo;
    private FirebaseUser user;

    public MainPresenter(MainView view, Repository repo) {
        this.view = view;
        this.repo = repo;
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
     * If user settings distance unit hasn't been initialized (user reset app or first time using it)
     * sets it to default value (mi)
     */
    public void initUserSettings() {
        //If values haven't been set, add default values
        if (repo.getDistanceUnit() == null) {
            repo.setDistanceUnit(Distance.Unit.MILE);
        }
    }

    public void onAboutClicked() {
        view.showAboutDialog();
    }

    public void onRateUsClicked() {
        view.sendPlayStoreRatingIntent();
    }


    public String getUserDisplayName(){
        return user.getDisplayName();
    }

    public String getUserEmail(){
        return user.getEmail();
    }

}
