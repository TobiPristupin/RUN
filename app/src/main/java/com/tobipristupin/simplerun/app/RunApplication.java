package com.tobipristupin.simplerun.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.database.FirebaseDatabase;
import com.tobipristupin.simplerun.BuildConfig;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * This class overrides the onCreate method of Application
 * in order to call setPersistenceEnabled(), which has to be called when app starts
 */

public class RunApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(new Locale("es"));
        resources.updateConfiguration(configuration,displayMetrics);
    }
}

