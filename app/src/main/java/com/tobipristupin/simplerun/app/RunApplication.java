package com.tobipristupin.simplerun.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

/**
 * This class overrides the onCreate method of Application
 * in order to call setPersistenceEnabled(), which has to be called when app starts.
 */

public class RunApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(new Locale("es"));
        resources.updateConfiguration(configuration,displayMetrics);
    }
}

