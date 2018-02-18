package com.tobipristupin.simplerun.ui.login;

import android.support.annotation.Nullable;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface NewAccountView {

    void setEmailTextInputError(boolean enabled, @Nullable String error);

    void setPasswordTextInputError(boolean enabled, @Nullable String error);

    void setPassword2TextInputError(boolean enabled, @Nullable String error);

    void sendIntentMainActivity();

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void showCreateAccountErrorToast();

    void showWeakPasswordErrorToast();

    void showUserCollisionToast();

    void sendGoogleSignInIntent();

    void showGoogleSignInFailedToast();
}
