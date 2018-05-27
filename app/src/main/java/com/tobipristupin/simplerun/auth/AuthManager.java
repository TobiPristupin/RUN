package com.tobipristupin.simplerun.auth;

import com.google.firebase.auth.AuthCredential;

import io.reactivex.Completable;


public interface AuthManager {

    Completable logInWithEmailAndPassword(String email, String password);

    Completable logInWithCredentials(AuthCredential credential);

    Completable sendResetPasswordEmail(String email);

    Completable createNewAccount(String email, String password);
}
