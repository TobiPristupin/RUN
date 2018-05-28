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
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.auth.AuthManager;
import com.tobipristupin.simplerun.utils.EmailValidator;
import com.tobipristupin.simplerun.utils.LogWrapper;
import com.tobipristupin.simplerun.utils.SingleLiveEvent;
import com.tobipristupin.simplerun.utils.VoidSingleLiveEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginViewModel extends ViewModel {

    private VoidSingleLiveEvent intentToMainActivityAction = new VoidSingleLiveEvent();
    private VoidSingleLiveEvent googleSignInIntentAction = new VoidSingleLiveEvent();
    private SingleLiveEvent<Integer> showErrorToastAction = new SingleLiveEvent<>();
    private VoidSingleLiveEvent openForgotPasswordPage = new VoidSingleLiveEvent();
    private VoidSingleLiveEvent openNewAccountPage = new VoidSingleLiveEvent();
    private MutableLiveData<Boolean> showLoadingAnimation = new MutableLiveData<>();
    private MutableLiveData<Integer> emailError = new MutableLiveData<>();
    private MutableLiveData<Integer> passwordError = new MutableLiveData<>();
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
        showErrorToast(R.string.login_google_error);
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
            passwordError.postValue(R.string.all_invalid_credentials);
        }
        if (e instanceof FirebaseAuthInvalidUserException) {
            emailError.postValue(R.string.all_username_doesnt_exist);
        } else {
            showErrorToast(R.string.login_error);
        }
    }

    private boolean fieldsAreValid(String email, String password){
        if (email.isEmpty()){
            emailError.postValue(R.string.all_required_field);
            return false;
        }
        if (password.isEmpty()){
            passwordError.postValue(R.string.all_required_field);
            return false;
        }
        if (!EmailValidator.isValid(email)){
            emailError.postValue(R.string.all_invalid_email);
            return false;
        }
        if (password.length() <= 6){
            passwordError.postValue(R.string.all_short_password);
            return false;
        }

        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    private void showErrorToast(int resId){
        showErrorToastAction.setValue(resId);
    }

    public VoidSingleLiveEvent getSendIntentToMainActivityAction() {
        return intentToMainActivityAction;
    }

    public MutableLiveData<Boolean> getShowLoadingAnimation() {
        return showLoadingAnimation;
    }

    public VoidSingleLiveEvent getGoogleSignInIntentAction() {
        return googleSignInIntentAction;
    }

    public SingleLiveEvent<Integer> getShowErrorToast() {
        return showErrorToastAction;
    }

    public VoidSingleLiveEvent getOpenForgotPasswordPageAction() {
        return openForgotPasswordPage;
    }

    public VoidSingleLiveEvent getOpenNewAccountPageAction() {
        return openNewAccountPage;
    }

    public MutableLiveData<Integer> getEmailError() {
        return emailError;
    }

    public MutableLiveData<Integer> getPasswordError() {
        return passwordError;
    }

}
