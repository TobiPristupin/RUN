package com.tobipristupin.simplerun.ui.login.loginview;



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


public class LoginPresenter {

    private static final String TAG = "LoginPresenter";
    private LoginView view;

    public LoginPresenter(LoginView view){
        this.view = view;
    }

    public void attemptEmailLogin(String email, String password){
        AuthManager authManager = new FirebaseAuthManager();

        if (!fieldsAreValid(email, password)){
            return;
        }

        view.startLoadingAnimation();

        authManager.logInWithEmailAndPassword(email, password, new AuthCallbacks.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                LoginPresenter.this.onLoginSuccess();
            }

            @Override
            public void onLoginFailed(Exception e) {
                LoginPresenter.this.onLoginFailed(e);
            }
        });
    }

    public void onGoogleLogInClick(){
        view.startLoadingAnimation();
        view.sendGoogleSignInIntent();
    }

    private void onLoginSuccess(){
        view.stopLoadingAnimation();
        view.sendIntentMainActivity();
    }

    private void onLoginFailed(Exception e){
        Crashlytics.logException(e);

        view.stopLoadingAnimation();
        if (e instanceof FirebaseAuthInvalidCredentialsException){
            view.enablePasswordError(ErrorType.PasswordLogin.INVALID_CREDENTIALS);
        } if (e instanceof FirebaseAuthInvalidUserException){
            view.enableEmailError(ErrorType.EmailLogin.USERNAME_DOESNT_EXIST);
        } else {
            view.showUnexpectedLoginErrorToast();
        }
    }

    private void onGoogleSignInSuccess(GoogleSignInResult result){
        LogWrapper.info(TAG, "HandleGoogleSignInResult: Success");
        GoogleSignInAccount account = result.getSignInAccount();
        authenticateGoogleLogin(account);
    }

    private void onGoogleSignInFailed(GoogleSignInResult result){
        LogWrapper.info(TAG, "HandleGoogleSignInResult: Failed. Status code " + result.getStatus().getStatusCode());
        view.stopLoadingAnimation();
        view.showGoogleSignInFailedToast();
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

    private void authenticateGoogleLogin(GoogleSignInAccount account){
        AuthManager authManager = new FirebaseAuthManager();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authManager.logInWithCredentials(credential, new AuthCallbacks.LoginCallback() {
            @Override
            public void onLoginSuccess() {
                LoginPresenter.this.onLoginSuccess();
            }

            @Override
            public void onLoginFailed(Exception e) {
                LoginPresenter.this.onLoginFailed(e);
            }
        });
    }

    private boolean fieldsAreValid(String email, String password){
        if (email.isEmpty()){
            view.enableEmailError(ErrorType.EmailLogin.REQUIRED_FIELD);
            return false;
        }
        if (password.isEmpty()){
            view.enablePasswordError(ErrorType.PasswordLogin.REQUIRED_FIELD);
            return false;
        }
        if (!EmailValidator.isValid(email)){
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
