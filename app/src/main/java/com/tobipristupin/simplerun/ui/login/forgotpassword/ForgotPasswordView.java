package com.tobipristupin.simplerun.ui.login.forgotpassword;

import android.support.annotation.Nullable;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface ForgotPasswordView {

    void enableEmailError();

    void disableEmailError();

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void showRecoveryEmailSentToast();

    void showRecoveryEmailFailedToast();

    void showTooManyRequestsToast();
}
