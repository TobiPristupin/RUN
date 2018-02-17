package com.example.tobias.run.ui.login;


import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.example.tobias.run.auth.FirebaseAuthManager;
import com.example.tobias.run.auth.interfaces.AuthCallbacks;
import com.example.tobias.run.auth.interfaces.AuthManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;

import org.apache.commons.validator.routines.EmailValidator;

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
             //Matching passwords have been validated, no need to pass both.
             view.startLoadingAnimation();
             createAccount(email, password);
         }
    }

    private boolean validateFields(String email, String password, String password2){
        EmailValidator validator = EmailValidator.getInstance();


        if (email.isEmpty()) {
            view.setEmailTextInputError(true, "Field is required");
            return false;
        } else if (password.isEmpty()) {
            view.setPasswordTextInputError(true, "Field is required");
            return false;
        } else if (password2.isEmpty()) {
            view.setPassword2TextInputError(true, "Field is required");
            return false;
        }

        if (!validator.isValid(email)) {
            view.setEmailTextInputError(true, "Invalid email");
            return false;
        }

        if (password.length() <= 6) {
            view.setPasswordTextInputError(true, "Password must be longer than 6 characters");
            return false;
        } else if (password2.length() <= 6) {
            view.setPassword2TextInputError(true, "Password must be longer than 6 characters");
            return false;
        }

        if (!password.equals(password2)){
            view.setPasswordTextInputError(true, "Passwords do not match");
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

    public void onEmailTextInputTextChanged(){
        view.setEmailTextInputError(false, null);
    }

    public void onPasswordTextInputTextChanged(){
        view.setPasswordTextInputError(false, null);
    }

    public void onPassword2TextInputTextChanged(){
        view.setPassword2TextInputError(false, null);
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
