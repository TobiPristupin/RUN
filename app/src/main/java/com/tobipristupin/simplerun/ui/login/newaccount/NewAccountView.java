package com.tobipristupin.simplerun.ui.login.newaccount;

import android.support.annotation.Nullable;

import com.tobipristupin.simplerun.interfaces.ErrorType;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface NewAccountView {

    void enableEmailError(ErrorType.EmailLogin type);

    void disableEmailError();

    void enablePasswordError(ErrorType.PasswordLogin type);

    void disablePasswordError();

    void enablePassword2Error(ErrorType.PasswordLogin type);

    void disablePassword2Error();

    void sendIntentMainActivity();

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void showCreateAccountErrorToast();

    void showWeakPasswordErrorToast();

    void showUserCollisionToast();

    void sendGoogleSignInIntent();

    void showGoogleSignInFailedToast();
}
