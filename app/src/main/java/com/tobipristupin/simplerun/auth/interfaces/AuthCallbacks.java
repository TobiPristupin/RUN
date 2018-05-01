package com.tobipristupin.simplerun.auth.interfaces;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface AuthCallbacks {

    interface LoginCallback{

        void onLoginSuccess();

        void onLoginFailed(Exception e);
    }

}
