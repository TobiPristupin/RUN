package com.tobipristupin.simplerun.ui.login.loginview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.auth.interfaces.AuthCallbacks;
import com.tobipristupin.simplerun.auth.interfaces.AuthManager;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.utils.EmailValidator;
import com.tobipristupin.simplerun.utils.LogWrapper;
import com.tobipristupin.simplerun.utils.SingleLiveEvent;

public class LoginViewModel extends ViewModel {

    private SingleLiveEvent<Void> intentToMainActivityAction = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> googleSignInIntentAction = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> showLoginErrorToast = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> showGoogleSignInErrorToast = new SingleLiveEvent<>();

    private MutableLiveData<Boolean> showLoadingAnimation = new MutableLiveData<>();
    private MutableLiveData<ErrorType.EmailLogin> emailError = new MutableLiveData<>();
    private MutableLiveData<ErrorType.PasswordLogin> passwordError = new MutableLiveData<>();
    private MutableLiveData<Integer> viewPagerPosition = new MutableLiveData<>();

    private static final String TAG = "LoginViewModel";
    

    public void attemptEmailLogin(String email, String password){
        AuthManager authManager = new FirebaseAuthManager();

        if (!fieldsAreValid(email, password)){
            return;
        }

        showLoadingAnimation.postValue(true);

        authManager.logInWithEmailAndPassword(email, password, new AuthCallbacks.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                LoginViewModel.this.onLoginSuccess();
            }

            @Override
            public void onLoginFailed(Exception e) {
                LoginViewModel.this.onLoginFailed(e);
            }
        });
    }

    public void attemptGoogleLogIn(){
        showLoadingAnimation.postValue(true);
        googleSignInIntentAction.call();
    }

    /**
     * sendGoogleSignInIntent will start an activity for result, which will then return its result
     * to view's onActivityResult which will call this method with the result of the login.
     * @param result
     */
    public void onGoogleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            onGoogleSignInSuccess(result);
        } else {
            onGoogleSignInFailed(result);
        }
    }

    public void onNewAccountClicked(){
        viewPagerPosition.postValue(2);
    }

    public void onForgotPasswordClicked(){
        viewPagerPosition.postValue(0);
    }

    private void onGoogleSignInSuccess(GoogleSignInResult result){
        LogWrapper.info(TAG, "HandleGoogleSignInResult: Success");
        GoogleSignInAccount account = result.getSignInAccount();
        authenticateGoogleLogin(account);
    }

    private void onGoogleSignInFailed(GoogleSignInResult result){
        LogWrapper.info(TAG, "HandleGoogleSignInResult: Failed. Status code " + result.getStatus().getStatusCode());
        showLoadingAnimation.postValue(false);
        showGoogleSignInErrorToast.call();
    }

    private void authenticateGoogleLogin(GoogleSignInAccount account){
        AuthManager authManager = new FirebaseAuthManager();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authManager.logInWithCredentials(credential, new AuthCallbacks.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                LoginViewModel.this.onLoginSuccess();
            }

            @Override
            public void onLoginFailed(Exception e) {
                LoginViewModel.this.onLoginFailed(e);
            }
        });
    }

    private void onLoginSuccess(){
        showLoadingAnimation.postValue(false);
        intentToMainActivityAction.call();
    }

    private void onLoginFailed(Exception e) {
        Crashlytics.logException(e);

        showLoadingAnimation.postValue(false);
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            passwordError.postValue(ErrorType.PasswordLogin.INVALID_CREDENTIALS);
        }
        if (e instanceof FirebaseAuthInvalidUserException) {
            emailError.postValue(ErrorType.EmailLogin.USERNAME_DOESNT_EXIST);
        } else {
            showLoginErrorToast.call();
        }
    }

    private boolean fieldsAreValid(String email, String password){
        if (email.isEmpty()){
            emailError.postValue(ErrorType.EmailLogin.REQUIRED_FIELD);
            return false;
        }
        if (password.isEmpty()){
            passwordError.postValue(ErrorType.PasswordLogin.REQUIRED_FIELD);
            return false;
        }
        if (!EmailValidator.isValid(email)){
            emailError.postValue(ErrorType.EmailLogin.INVALID_EMAIL);
            return false;
        }
        if (password.length() <= 6){
            passwordError.postValue(ErrorType.PasswordLogin.SHORT_PASSWORD);
            return false;
        }

        return true;
    }

    
    public SingleLiveEvent<Void> getSendIntentToMainActivityAction() {
        return intentToMainActivityAction;
    }

    public MutableLiveData<Boolean> getShowLoadingAnimation() {
        return showLoadingAnimation;
    }

    public SingleLiveEvent<Void> getGoogleSignInIntentAction() {
        return googleSignInIntentAction;
    }

    public SingleLiveEvent<Void> getShowLoginErrorToast() {
        return showLoginErrorToast;
    }

    public SingleLiveEvent<Void> getShowGoogleSignInErrorToastAction() {
        return showGoogleSignInErrorToast;
    }

    public MutableLiveData<ErrorType.EmailLogin> getEmailError() {
        return emailError;
    }

    public MutableLiveData<ErrorType.PasswordLogin> getPasswordError() {
        return passwordError;
    }

    public MutableLiveData<Integer> getViewPagerPosition() {
        return viewPagerPosition;
    }
}
