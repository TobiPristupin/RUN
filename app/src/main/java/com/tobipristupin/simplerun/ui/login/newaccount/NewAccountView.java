package com.tobipristupin.simplerun.ui.login.newaccount;

import android.support.annotation.Nullable;

import com.tobipristupin.simplerun.interfaces.ErrorType;


public interface NewAccountView {

    void enableEmailError(ErrorType.EmailLogin type);

    void enablePasswordError(ErrorType.PasswordLogin type);

    void enablePassword2Error(ErrorType.PasswordLogin type);

    void sendIntentMainActivity();

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void showCreateAccountErrorToast();

    void showWeakPasswordErrorToast();

    void showUserCollisionToast();

    void sendGoogleSignInIntent();

    void showGoogleSignInFailedToast();
}
