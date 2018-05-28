package com.tobipristupin.simplerun.ui.login.forgotpassword;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.auth.AuthManager;
import com.tobipristupin.simplerun.utils.EmailValidator;
import com.tobipristupin.simplerun.utils.SingleLiveEvent;
import com.tobipristupin.simplerun.utils.VoidSingleLiveEvent;

import io.reactivex.disposables.Disposable;


public class ForgotPasswordViewModel extends ViewModel {

    private MutableLiveData<Integer> emailError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private SingleLiveEvent<Integer> showSuccessToastAction = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> showErrorToastAction = new SingleLiveEvent<>();
    private VoidSingleLiveEvent openLoginPage = new VoidSingleLiveEvent();
    private Disposable resetEmailSubscription;

    public void onSendEmailButtonClick(String email){
        if (!EmailValidator.isValid(email)){
            emailError.postValue(R.string.all_invalid_email);
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
            showErrorToast(R.string.forgot_password_toomanyrequests);
        } else {
            showErrorToast(R.string.forgot_password_recovery_failed);
        }
    }

    private void onResetPasswordSuccess(){
        loading.postValue(false);
        showSuccessToast(R.string.forgot_password_recovery_sent);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (resetEmailSubscription != null){
            resetEmailSubscription.dispose();
        }
    }

    private void showSuccessToast(int resId){
        showSuccessToastAction.setValue(resId);
    }

    private void showErrorToast(int resId){
        showErrorToastAction.setValue(resId);
    }

    public MutableLiveData<Integer> getEmailError() {
        return emailError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public SingleLiveEvent<Integer> getShowSuccessToast() {
        return showSuccessToastAction;
    }

    public SingleLiveEvent<Integer> getShowErrorToast() {
        return showErrorToastAction;
    }

    public VoidSingleLiveEvent getOpenLoginPage() {
        return openLoginPage;
    }
}
