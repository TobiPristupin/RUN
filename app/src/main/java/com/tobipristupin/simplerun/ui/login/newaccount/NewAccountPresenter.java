package com.tobipristupin.simplerun.ui.login.newaccount;


import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.auth.interfaces.AuthCallbacks;
import com.tobipristupin.simplerun.auth.interfaces.AuthManager;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.utils.EmailValidator;

/**
 * Created by Tobi on 10/22/2017.
 */

public class NewAccountPresenter {

    public static final String TAG = "NewAccountPresenter";
    private NewAccountView view;

    public NewAccountPresenter(NewAccountView view) {
        this.view = view;
    }

    public void onCreateAccountButtonClick(String email, String password, String password2){
         if (validateFields(email, password, password2)){
             view.startLoadingAnimation();
             //Matching passwords have been validated, no need to pass both.
             createAccount(email, password);
         }
    }

    private boolean validateFields(String email, String password, String password2){
        if (email.isEmpty()) {
            view.enableEmailError(ErrorType.EmailLogin.REQUIRED_FIELD);
            return false;
        } else if (password.isEmpty()) {
            view.enablePasswordError(ErrorType.PasswordLogin.REQUIRED_FIELD);
            return false;
        } else if (password2.isEmpty()) {
            view.enablePassword2Error(ErrorType.PasswordLogin.REQUIRED_FIELD);
            return false;
        }

        if (!EmailValidator.isValid(email)) {
            view.enableEmailError(ErrorType.EmailLogin.INVALID_EMAIL);
            return false;
        }

        if (password.length() <= 6) {
            view.enablePasswordError(ErrorType.PasswordLogin.SHORT_PASSWORD);
            return false;
        } else if (password2.length() <= 6) {
            view.enablePassword2Error(ErrorType.PasswordLogin.SHORT_PASSWORD);
            return false;
        }

        if (!password.equals(password2)){
            view.enablePasswordError(ErrorType.PasswordLogin.PASSWORD_DONT_MATCH);
            return false;
        }

        return true;

    }

    private void createAccount(String email, String password){
        AuthManager authManager = new FirebaseAuthManager();
        authManager.createNewAccount(email, password, new AuthCallbacks.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                view.stopLoadingAnimation();
                view.sendIntentMainActivity();
            }

            @Override
            public void onLoginFailed(Exception e) {
                Crashlytics.logException(e);
                view.stopLoadingAnimation();
                if (e instanceof FirebaseAuthWeakPasswordException){
                    view.showWeakPasswordErrorToast();
                } else if (e instanceof FirebaseAuthUserCollisionException){
                    view.showUserCollisionToast();
                } else {
                    view.showCreateAccountErrorToast();
                }

            }
        });
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
            authenticateGoogleSignIn(account);
        } else {
            Log.d(TAG, "HandleGoogleSignInResult: Failed. Status code " + result.getStatus().getStatusCode());
            view.stopLoadingAnimation();
            view.showGoogleSignInFailedToast();
        }
    }

    private void authenticateGoogleSignIn(GoogleSignInAccount account){
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
                view.stopLoadingAnimation();
                view.showGoogleSignInFailedToast();
            }
        });
    }



}
