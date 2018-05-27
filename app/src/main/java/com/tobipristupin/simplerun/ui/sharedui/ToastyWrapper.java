package com.tobipristupin.simplerun.ui.sharedui;


import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Wrapper class for toastys that handles cancelling the toast when multiple toasts are shown
 * to avoid stacking them.
 */

public class ToastyWrapper {

    private Toast instance;

    public void showWarning(Context context, String text){
        destroyPreviousInstance();
        instance = Toasty.warning(context, text, Toast.LENGTH_SHORT);
        instance.show();
    }

    public void showWarning(Context context, String text, int length){
        destroyPreviousInstance();
        instance = Toasty.warning(context, text, length);
        instance.show();
    }

    public void showSuccess(Context context, String text, int length){
        destroyPreviousInstance();
        instance = Toasty.success(context, text, length);
        instance.show();
    }

    public void showSuccess(Context context, String text){
        destroyPreviousInstance();
        instance = Toasty.success(context, text, Toast.LENGTH_SHORT);
        instance.show();
    }

    private void destroyPreviousInstance(){
        if (instance != null){
            instance.cancel();
        }
    }
}
