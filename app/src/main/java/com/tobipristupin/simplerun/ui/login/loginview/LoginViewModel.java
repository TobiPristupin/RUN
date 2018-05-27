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
import com.tobipristupin.simplerun.auth.AuthManager;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.utils.EmailValidator;
import com.tobipristupin.simplerun.utils.LogWrapper;
import com.tobipristupin.simplerun.utils.VoidLiveAction;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginViewModel extends ViewModel {

    private VoidLiveAction intentToMainActivityAction = new VoidLiveAction();
    private VoidLiveAction googleSignInIntentAction = new VoidLiveAction();
    private VoidLiveAction showLoginErrorToast = new VoidLiveAction();
    private VoidLiveAction showGoogleSignInErrorToast = new VoidLiveAction();
    private VoidLiveAction openForgotPasswordPage = new VoidLiveAction();
    private VoidLiveAction openNewAccountPage = new VoidLiveAction();
    private MutableLiveData<Boolean> showLoadingAnimation = new MutableLiveData<>();
    private MutableLiveData<ErrorType.EmailLogin> emailError = new MutableLiveData<>();
    private MutableLiveData<ErrorType.PasswordLogin> passwordError = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "LoginViewModel";
    

    public void attemptEmailLogin(String email, String password){
        if (!fieldsAreValid(email, password)){
            return;
        }

        showLoadingAnimation.postValue(true);

        AuthManager authManager = new FirebaseAuthManager();
        Disposable d = authManager.logInWithEmailAndPassword(email, password)
                .subscribe(() -> {
                    onLoginSuccess();
                }, throwable -> {
                    onLoginFailed(throwable);
                });

        compositeDisposable.add(d);
    }

    public void attemptGoogleLogIn(){
        showLoadingAnimation.postValue(true);
        googleSignInIntentAction.call();
    }

    /**
     * googleSignInIntent will start an activity for result, which will then return its result
     * to view's onActivityResult which will call this method with the result of the login.
     */
    public void onGoogleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            onGoogleSignInSuccess(result);
        } else {
            onGoogleSignInFailed(result);
        }
    }

    public void onNewAccountClicked(){
        openNewAccountPage.call();
    }

    public void onForgotPasswordClicked(){
        openForgotPasswordPage.call();
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

        Disposable d = authManager.logInWithCredentials(credential)
                .subscribe(() -> {
                    onLoginSuccess();
                }, throwable -> {
                    onLoginFailed(throwable);
                });

        compositeDisposable.add(d);
    }

    private void onLoginSuccess(){
        showLoadingAnimation.postValue(false);
        intentToMainActivityAction.call();
    }

    private void onLoginFailed(Throwable e) {
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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public VoidLiveAction getSendIntentToMainActivityAction() {
        return intentToMainActivityAction;
    }

    public MutableLiveData<Boolean> getShowLoadingAnimation() {
        return showLoadingAnimation;
    }

    public VoidLiveAction getGoogleSignInIntentAction() {
        return googleSignInIntentAction;
    }

    public VoidLiveAction getShowLoginErrorToast() {
        return showLoginErrorToast;
    }

    public VoidLiveAction getShowGoogleSignInErrorToastAction() {
        return showGoogleSignInErrorToast;
    }

    public VoidLiveAction getOpenForgotPasswordPageAction() {
        return openForgotPasswordPage;
    }

    public VoidLiveAction getOpenNewAccountPageAction() {
        return openNewAccountPage;
    }

    public MutableLiveData<ErrorType.EmailLogin> getEmailError() {
        return emailError;
    }

    public MutableLiveData<ErrorType.PasswordLogin> getPasswordError() {
        return passwordError;
    }

}
