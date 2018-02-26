package com.tobipristupin.simplerun.ui.login.forgotpassword;

public interface ForgotPasswordView {

    void enableEmailError();

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void showRecoveryEmailSentToast();

    void showRecoveryEmailFailedToast();

    void showTooManyRequestsToast();
}
