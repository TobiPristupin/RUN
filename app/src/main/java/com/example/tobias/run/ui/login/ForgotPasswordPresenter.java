package com.example.tobias.run.ui.login;

import com.crashlytics.android.Crashlytics;
import com.example.tobias.run.auth.FirebaseAuthManager;
import com.example.tobias.run.auth.interfaces.AuthCallbacks;
import com.example.tobias.run.auth.interfaces.AuthManager;
import com.google.firebase.FirebaseTooManyRequestsException;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Created by Tobi on 10/22/2017.
 */

public class ForgotPasswordPresenter {

    private ForgotPasswordView view;

    public ForgotPasswordPresenter(ForgotPasswordView view){
        this.view = view;
    }

    public void onEmailTextInputTextChanged(){
        view.setEmailTextInputError(false, null);
    }

    public void onSendEmailButtonClicked(String email){
        if (isValidEmail(email)){
            view.startLoadingAnimation();
            sendResetPasswordEmail(email);
        } else {
            view.setEmailTextInputError(true, "Invalid Email");
        }
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
            }
        });
    }


}
