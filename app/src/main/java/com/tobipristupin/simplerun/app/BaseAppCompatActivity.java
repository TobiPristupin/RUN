package com.tobipristupin.simplerun.app;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.tobipristupin.simplerun.R;

/**
 * Base class that implements methods common to all activities
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    /**
     * AppTheme status bar color attr is set to transparent for the drawerLayout in main activity.
     * this activity uses the primary dark color as status bar color. This method sets it during runtime.
     */
    protected void changeStatusBarColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(resId));
        }
    }

    /**
     * Sets toolbar defined in xml as default support action toolbar and adds
     * homeAsUpIndicator
     * @param viewId
     */
    protected void setSupportActionBar(int viewId) {
        Toolbar toolbar = findViewById(viewId);
        setSupportActionBar(toolbar);
        setHomeAsUpEnabled();
    }

    private void setHomeAsUpEnabled() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setHomeAsUpDrawable(int drawable){
        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }


    /**
     * Sets toolbar defined in xml as default support action toolbar and adds
     * homeAsUpIndicator with drawable
     *
     * @param viewId toolbar id
     * @param homeAsUpDrawable drawable id to be used in homeAsUpIndicator
     */
    protected void setSupportActionBar(int viewId, int homeAsUpDrawable) {
        setSupportActionBar(viewId);
        setHomeAsUpDrawable(homeAsUpDrawable);
    }


}
