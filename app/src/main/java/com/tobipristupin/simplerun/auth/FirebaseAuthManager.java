package com.tobipristupin.simplerun.auth;


import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.tobipristupin.simplerun.utils.LogWrapper;
import io.reactivex.Completable;



public class FirebaseAuthManager implements AuthManager {

    private static final String TAG = "FirebaseAuthManager";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public Completable logInWithEmailAndPassword(String email, String password) {
        return Completable.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    logSuccess("FirebaseLogInEmailAndPassword");
                    emitter.onComplete();
                } else {
                    logError(task.getException(), "FirebaseLogInEmailAndPassword: Failed " + task.getException());
                    emitter.onError(task.getException());
                }
            });
        });
    }

    @Override
    public Completable logInWithCredentials(AuthCredential credential) {
        return Completable.create(emitter -> {
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    logSuccess("FirebaseLogInWithCredentials: Success");
                    emitter.onComplete();
                } else {
                    logError(task.getException(), "FirebaseCreateNewAccount: Failed " + task.getException());
                    emitter.onError(task.getException());
                }
            });
        });
    }

    @Override
    public Completable sendResetPasswordEmail(String email) {
        return Completable.create(emitter -> {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    logSuccess("FirebaseSendPasswordResetEmail: Success");
                    emitter.onComplete();
                } else {
                    logError(task.getException(), "FirebaseSendPasswordResetEmail: Failed " + task.getException());
                    emitter.onError(task.getException());
                }
            });
        });
    }

    @Override
    public Completable createNewAccount(String email, String password) {
        return Completable.create(emitter -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    logSuccess("FirebaseCreateAccount: Success");
                    emitter.onComplete();
                } else {
                    logError(task.getException(), "FirebaseCreateAccount: Failed " + task.getException());
                    emitter.onError(task.getException());
                }
            });
        });
    }

    private void logError(Exception e, String msg){
        LogWrapper.warn(TAG, msg);
        Crashlytics.logException(e);
    }

    private void logSuccess(String msg){
        LogWrapper.info(TAG, msg);
    }
}
