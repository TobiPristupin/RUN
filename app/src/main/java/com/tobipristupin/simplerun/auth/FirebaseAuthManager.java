package com.tobipristupin.simplerun.auth;

import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tobipristupin.simplerun.auth.interfaces.AuthCallbacks;
import com.tobipristupin.simplerun.auth.interfaces.AuthManager;
import com.tobipristupin.simplerun.utils.LogWrapper;



public class FirebaseAuthManager implements AuthManager {

    private static final String TAG = "FirebaseAuthManager";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public void logInWithEmailAndPassword(String email, String password, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LogWrapper.info(TAG, "FirebaseSignInWithEmail: Successful");
                callback.onLoginSuccess();
            } else {
                LogWrapper.warn(TAG, "FirebaseSignInWithEmail: Failed "  + task.getException());
                Crashlytics.logException(task.getException());
                callback.onLoginFailed(task.getException());
            }
        });
    }

    @Override
    public void logInWithCredentials(AuthCredential credential, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LogWrapper.info(TAG, "FirebaseSignInWithCredentials: Success");
                callback.onLoginSuccess();
            } else {
                LogWrapper.warn(TAG, "FirebaseSignInWithCredentials: Failed " + task.getException());
                Crashlytics.logException(task.getException());
                callback.onLoginFailed(task.getException());
            }
        });
    }

    @Override
    public void sendResetPasswordEmail(String email, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LogWrapper.info(TAG, "FirebaseSendResetEmail: Successful");
                callback.onLoginSuccess();
            } else {
                LogWrapper.warn(TAG, "FirebaseSendResetEmail: Failed " + task.getException());
                Crashlytics.logException(task.getException());
                callback.onLoginFailed(task.getException());
            }
        });
    }

    @Override
    public void createNewAccount(String email, String password, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                LogWrapper.info(TAG, "FirebaseCreateNewAccount: Successful");
                callback.onLoginSuccess();
            } else {
                LogWrapper.warn(TAG, "FirebaseCreateNewAccount: Failed " + task.getException());
                Crashlytics.logException(task.getException());
                callback.onLoginFailed(task.getException());
            }
        });
    }

}
