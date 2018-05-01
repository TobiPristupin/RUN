package com.tobipristupin.simplerun.auth.interfaces;

import com.google.firebase.auth.AuthCredential;


public interface AuthManager {

    void logInWithEmailAndPassword(String email, String password, AuthCallbacks.LoginCallback callback);

    void logInWithCredentials(AuthCredential credential, AuthCallbacks.LoginCallback callback);

    void sendResetPasswordEmail(String email, AuthCallbacks.LoginCallback callback);

    void createNewAccount(String email, String password, AuthCallbacks.LoginCallback callback);
}
