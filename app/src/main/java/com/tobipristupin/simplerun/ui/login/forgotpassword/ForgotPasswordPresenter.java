package com.tobipristupin.simplerun.ui.login.forgotpassword;

import com.crashlytics.android.Crashlytics;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.auth.interfaces.AuthCallbacks;
import com.tobipristupin.simplerun.auth.interfaces.AuthManager;
import com.google.firebase.FirebaseTooManyRequestsException;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Locale;


public class ForgotPasswordPresenter {

    private ForgotPasswordView view;

    public ForgotPasswordPresenter(ForgotPasswordView view){
        this.view = view;
    }

    public void onEmailTextInputTextChanged(){
        view.disableEmailError();
    }

    public void onSendEmailButtonClicked(String email){
        if (isValidEmail(email)){
            view.startLoadingAnimation();
            sendResetPasswordEmail(email);
            return;
        }

        view.enableEmailError();

    }

    private boolean isValidEmail(String email){
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    private void sendResetPasswordEmail(String email){
        AuthManager manager = new FirebaseAuthManager();
        manager.sendResetPasswordEmail(email, new AuthCallbacks.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                view.showRecoveryEmailSentToast();
                view.stopLoadingAnimation();
            }

            @Override
            public void onLoginFailed(Exception e) {
                Crashlytics.logException(e);
                if (e instanceof FirebaseTooManyRequestsException){
                    view.showTooManyRequestsToast();
                } else {
                    view.showRecoveryEmailFailedToast();
                }

                view.stopLoadingAnimation();
            }
        });
    }


}
