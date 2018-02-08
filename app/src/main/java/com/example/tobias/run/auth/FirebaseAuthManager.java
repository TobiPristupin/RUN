package com.example.tobias.run.auth;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tobias.run.auth.interfaces.AuthCallbacks;
import com.example.tobias.run.auth.interfaces.AuthManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Tobi on 10/22/2017.
 */

public class FirebaseAuthManager implements AuthManager {

    public static final String TAG = "FirebaseAuthManager";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public void logInWithEmailAndPassword(String email, String password, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "FirebaseSignInWithEmail: Successful");
                    callback.onLoginSuccess();
                } else {
                    Log.w(TAG, "FirebaseSignInWithEmail: Failed "  + task.getException());
                    callback.onLoginFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void logInWithCredentials(AuthCredential credential, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "FirebaseSignInWithCredentials: Successful");
                    callback.onLoginSuccess();
                } else {
                    Log.w(TAG, "FirebaseSignInWithCredentials: Failed " + task.getException());
                    callback.onLoginFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void sendResetPasswordEmail(String email, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "FirebaseSendResetEmail: Successful");
                    callback.onLoginSuccess();
                } else {
                    Log.w(TAG, "FirebaseSendResetEmail: Failed " + task.getException());
                    callback.onLoginFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void createNewAccount(String email, String password, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "FirebaseCreateNewAccount: Successful");
                    callback.onLoginSuccess();
                } else {
                    Log.w(TAG, "FirebaseCreateNewAccount: Failed " + task.getException());
                    callback.onLoginFailed(task.getException());
                }
            }
        });
    }
}
