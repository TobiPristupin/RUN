package com.tobipristupin.simplerun.ui.login.loginview;

import com.tobipristupin.simplerun.interfaces.ErrorType;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface LoginView {

    void enableEmailError(ErrorType.EmailLogin type);

    void enablePasswordError(ErrorType.PasswordLogin type);

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void sendIntentMainActivity();

    void showUnexpectedLoginErrorToast();

    void sendGoogleSignInIntent();

    void showGoogleSignInFailedToast();
}
