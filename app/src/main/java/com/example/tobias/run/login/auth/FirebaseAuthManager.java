package com.example.tobias.run.login.auth;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tobias.run.interfaces.AuthCallbacks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Tobi on 10/22/2017.
 */

public class FirebaseAuthManager implements AuthManager  {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static final String TAG = "FirebaseAuthManager";


    @Override
    public void logInWithEmailAndPassword(String email, String password, final AuthCallbacks.LoginCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "FirebaseSignInWithEmail: Successful");
                    callback.onLoginSuccess();
                } else {
                    Log.d(TAG, "FirebaseSignInWithEmail: Failed "  + task.getException());
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
                    Log.d(TAG, "FirebaseSignInWithCredentials: Failed " + task.getException());
                    callback.onLoginFailed(task.getException());
                }
            }
        });
    }
}
