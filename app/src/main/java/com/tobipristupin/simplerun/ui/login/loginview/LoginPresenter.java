package com.tobipristupin.simplerun.ui.login.loginview;


import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.auth.interfaces.AuthCallbacks;
import com.tobipristupin.simplerun.auth.interfaces.AuthManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tobipristupin.simplerun.interfaces.ErrorType;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Locale;


public class LoginPresenter {

    public static final String TAG = "LoginPresenter";
    private LoginView view;

    public LoginPresenter(LoginView view){
        this.view = view;
    }

    public void attemptEmailLogin(String email, String password){
        AuthManager authManager = new FirebaseAuthManager();

        if (fieldsAreValid(email, password)) {
            view.startLoadingAnimation();

            authManager.logInWithEmailAndPassword(email, password, new AuthCallbacks.LoginCallback() {
                @Override
                public void onLoginSuccess() {
                    view.stopLoadingAnimation();
                    view.sendIntentMainActivity();
                }

                @Override
                public void onLoginFailed(Exception e) {
                    Crashlytics.logException(e);

                    view.stopLoadingAnimation();
                    if (e instanceof FirebaseAuthInvalidCredentialsException){
                        view.enablePasswordError(ErrorType.PasswordLogin.INVALID_CREDENTIALS);
                    } else {
                        view.showUnexpectedLoginErrorToast();
                    }
                }
            });
        }
    }

    public void onGoogleLogInClick(){
        view.startLoadingAnimation();
        view.sendGoogleSignInIntent();
    }

    /**
     * sendGoogleSignInIntent will start an activity for result, which will then return its result
     * to view's onActivityResult which will call this method with the result of the login.
     * @param result
     */
    public void onGoogleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            Log.d(TAG, "HandleGoogleSignInResult: Success");
            GoogleSignInAccount account = result.getSignInAccount();

            authenticateGoogleLogin(account);
        } else {
            Log.d(TAG, "HandleGoogleSignInResult: Failed. Status code " + result.getStatus().getStatusCode());
            view.stopLoadingAnimation();
            view.showGoogleSignInFailedToast();
        }
    }

    private void authenticateGoogleLogin(GoogleSignInAccount account){
        AuthManager authManager = new FirebaseAuthManager();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authManager.logInWithCredentials(credential, new AuthCallbacks.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                view.stopLoadingAnimation();
                view.sendIntentMainActivity();
            }

            @Override
            public void onLoginFailed(Exception e) {
                Crashlytics.logException(e);
                view.stopLoadingAnimation();
                view.showGoogleSignInFailedToast();
            }
        });
    }

    public boolean fieldsAreValid(String email, String password){
        EmailValidator validator = EmailValidator.getInstance();

        if (email.isEmpty()){
            view.enableEmailError(ErrorType.EmailLogin.REQUIRED_FIELD);
            return false;
        }
        if (password.isEmpty()){
            view.enablePasswordError(ErrorType.PasswordLogin.REQUIRED_FIELD);
            return false;
        }
        if (!validator.isValid(email)){
            view.enableEmailError(ErrorType.EmailLogin.INVALID_EMAIL);
            return false;
        }
        if (password.length() <= 6){
            view.enablePasswordError(ErrorType.PasswordLogin.SHORT_PASSWORD);
            return false;
        }

        return true;
    }
}
