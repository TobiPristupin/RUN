package com.tobipristupin.simplerun.ui.sharedui;


import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Wrapper class for toasts that handles cancelling the toast when multiple toasts are shown
 * to avoid stacking them.
 */

public class ToastyWrapper {

    private Toast lastShown;

    public void show(Toast t){
        destroyPreviousInstance();
        lastShown = t;
        lastShown.show();
    }

    public void showWarning(Context context, String text){
        destroyPreviousInstance();
        lastShown = Toasty.warning(context, text, Toast.LENGTH_SHORT);
        lastShown.show();
    }

    public void showWarning(Context context, String text, int length){
        destroyPreviousInstance();
        lastShown = Toasty.warning(context, text, length);
        lastShown.show();
    }

    public void showSuccess(Context context, String text, int length){
        destroyPreviousInstance();
        lastShown = Toasty.success(context, text, length);
        lastShown.show();
    }

    public void showSuccess(Context context, String text){
        destroyPreviousInstance();
        lastShown = Toasty.success(context, text, Toast.LENGTH_SHORT);
        lastShown.show();
    }

    private void destroyPreviousInstance(){
        if (lastShown != null){
            lastShown.cancel();
        }
    }
}
