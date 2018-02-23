package com.tobipristupin.simplerun.ui;


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
        if (instance != null){
            instance.cancel();
        }

        instance = Toasty.warning(context, text, Toast.LENGTH_SHORT);
        instance.show();
    }

    public void showWarning(Context context, String text, int length){
        if (instance != null){
            instance.cancel();
        }

        instance = Toasty.warning(context, text, length);
        instance.show();
    }

    public void showSuccess(Context context, String text, int length){
        if (instance != null){
            instance.cancel();
        }

        instance = Toasty.success(context, text, length);
        instance.show();
    }

    public void showSuccess(Context context, String text){
        if (instance != null){
            instance.cancel();
        }

        instance = Toasty.success(context, text, Toast.LENGTH_SHORT);
        instance.show();
    }
}
