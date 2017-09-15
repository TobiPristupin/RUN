package com.example.tobias.run.app;

import android.app.Application;
import android.content.Intent;

import com.example.tobias.run.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class overrides the onCreate method of Application
 * in order to run database.setPersistenceEnabled(), which has to be called when app starts.
 */

public class RunApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}

