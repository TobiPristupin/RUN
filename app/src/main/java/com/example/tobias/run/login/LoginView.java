package com.example.tobias.run.login;

import android.support.annotation.Nullable;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface LoginView {
    void setEmailTextInputError(boolean enabled, @Nullable String error);

    void setPasswordTextInputError(boolean enabled, @Nullable String error);

    void startLoadingAnimation();

    void stopLoadingAnimation();

    void sendIntentMainActivity();

    void showUnexpectedLoginErrorToast();

    void sendGoogleSignInIntent();

    void showGoogleSignInFailedToast();
}
