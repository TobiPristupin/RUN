package com.tobipristupin.simplerun.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tobipristupin.simplerun.BuildConfig;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class RunApplication extends MultiDexApplication {

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        RunApplication application = (RunApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (isInAnalyzerProcess()) {
            return;
        }

        initLeakCanary();
        initFirebase();
        initCrashylitics();
    }

    /**
     * If app is in leak canary analyzer process it shouldn't be initialized because
     * that process is dedicated to Leak Canary for heap analysis
     */
    private boolean isInAnalyzerProcess(){
        return LeakCanary.isInAnalyzerProcess(this);
    }

    private void initFirebase(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private void initCrashylitics(){
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());
    }

    private void initLeakCanary(){
        refWatcher = LeakCanary.install(this);
    }

    /**
     * Use only for testing
     */
    private void enableSpanishLocale(){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(new Locale("es"));
        resources.updateConfiguration(configuration, displayMetrics);
    }

}

