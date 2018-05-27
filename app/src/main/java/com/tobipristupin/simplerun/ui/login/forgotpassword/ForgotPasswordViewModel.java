package com.tobipristupin.simplerun.ui.login.forgotpassword;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.auth.AuthManager;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.utils.EmailValidator;
import com.tobipristupin.simplerun.utils.VoidLiveAction;

import io.reactivex.disposables.Disposable;


public class ForgotPasswordViewModel extends ViewModel {

    private MutableLiveData<ErrorType.EmailLogin> emailError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private VoidLiveAction showEmailSentToast = new VoidLiveAction();
    private VoidLiveAction showTooManyRequestsToast = new VoidLiveAction();
    private VoidLiveAction showEmailFailedToast = new VoidLiveAction();
    private VoidLiveAction openLoginPage = new VoidLiveAction();
    private Disposable resetEmailSubscription;

    public void onSendEmailButtonClick(String email){
        if (!EmailValidator.isValid(email)){
            emailError.postValue(ErrorType.EmailLogin.INVALID_EMAIL);
            return;
        }

        loading.postValue(true);
        sendResetPasswordEmail(email);
    }

    public void onReturnButtonClick(){
        openLoginPage.call();
    }

    private void sendResetPasswordEmail(String email){
        AuthManager authManager = new FirebaseAuthManager();
        resetEmailSubscription = authManager.sendResetPasswordEmail(email)
                .subscribe(() -> {
                    onResetPasswordSuccess();
                }, throwable -> {
                    onResetPasswordFail(throwable);
                });
    }

    private void onResetPasswordFail(Throwable throwable) {
        Crashlytics.logException(throwable);
        loading.postValue(false);
        if (throwable instanceof FirebaseTooManyRequestsException){
            showTooManyRequestsToast.call();
        } else {
            showEmailFailedToast.call();
        }
    }

    private void onResetPasswordSuccess(){
        loading.postValue(false);
        showEmailSentToast.call();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (resetEmailSubscription != null){
            resetEmailSubscription.dispose();
        }
    }

    public MutableLiveData<ErrorType.EmailLogin> getEmailError() {
        return emailError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public VoidLiveAction getShowEmailSentToast() {
        return showEmailSentToast;
    }

    public VoidLiveAction getShowTooManyRequestsToast() {
        return showTooManyRequestsToast;
    }

    public VoidLiveAction getShowEmailFailedToast() {
        return showEmailFailedToast;
    }

    public VoidLiveAction getOpenLoginPage() {
        return openLoginPage;
    }
}
