package com.tobipristupin.simplerun.ui.login;

import android.support.annotation.Nullable;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface ForgotPasswordView {

    void setEmailTextInputError(boolean enabled, @Nullable String error);

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void showRecoveryEmailSentToast();

    void showRecoveryEmailFailedToast();

    void showTooManyRequestsToast();
}
