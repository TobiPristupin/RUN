package com.example.tobias.run.auth;

import com.example.tobias.run.interfaces.AuthCallbacks;
import com.google.firebase.auth.AuthCredential;

/**
 * Created by Tobi on 10/22/2017.
 */

public interface AuthManager {

    void logInWithEmailAndPassword(String email, String password, AuthCallbacks.LoginCallback callback);

    void logInWithCredentials(AuthCredential credential, AuthCallbacks.LoginCallback callback);

    void sendResetPasswordEmail(String email, AuthCallbacks.LoginCallback callback);

    void createNewAccount(String email, String password, AuthCallbacks.LoginCallback callback);
}
