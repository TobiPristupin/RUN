package com.tobipristupin.simplerun.app;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

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
     * Sets toolbar defined in xml as default support action toolbar. If homeAsUpIndicator is
     * true adds the homeAsUpIndicator
     * @param viewId
     * @param homeAsUpIndicator
     * @return toolbar view
     */
    protected Toolbar initToolbar(int viewId, boolean homeAsUpIndicator){
        Toolbar toolbar;

        try {
            toolbar = findViewById(viewId);
        } catch (ClassCastException e) {
            throw new ClassCastException("Wtf what is wrong with you? you're supposed to pass a toolbar view what are you doing");
        }

        setSupportActionBar(toolbar);

        if (homeAsUpIndicator) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        return toolbar;
    }

    /**
     * Sets toolbar defined in xml as default support action toolbar. This method always enables the
     * homeAsUpIndicator and uses the drawable passed, contrary to overloaded version of this method who enables it based on arguments.
     *
     * @param viewId toolbar id
     * @param homeAsUpDrawable drawable id to be used in homeAsUpIndicator
     * @return toolbar view
     */
    protected Toolbar initToolbar(int viewId, int homeAsUpDrawable){
        Toolbar toolbar = initToolbar(viewId, true);
        getSupportActionBar().setHomeAsUpIndicator(homeAsUpDrawable);
        return toolbar;
    }
}
