package com.tobipristupin.simplerun.ui.login.newaccount;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.auth.AuthManager;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.utils.EmailValidator;
import com.tobipristupin.simplerun.utils.LogWrapper;
import com.tobipristupin.simplerun.utils.SingleLiveEvent;
import com.tobipristupin.simplerun.utils.VoidSingleLiveEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class NewAccountViewModel extends ViewModel {
    
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String TAG = "NewAccountViewModel";

    private MutableLiveData<Integer> emailError = new MutableLiveData<>();
    private MutableLiveData<Integer> passwordError = new MutableLiveData<>();
    private MutableLiveData<Integer> password2Error = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private VoidSingleLiveEvent sendIntentMainActivity = new VoidSingleLiveEvent();
    private VoidSingleLiveEvent sendGoogleSignInIntent = new VoidSingleLiveEvent();
    private SingleLiveEvent<Integer> showErrorToast = new SingleLiveEvent<>();

    public void onCreateAccountClick(String email, String password1, String password2){
        if (!validFields(email, password1, password2)){
            return;
        }

        loading.postValue(true);
        createAccount(email, password1);
    }

    public void onGoogleLogInClick(){
        loading.postValue(true);
        sendGoogleSignInIntent.call();
    }

    public void onGoogleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            LogWrapper.info(TAG, "HandleGoogleSignInResult: Success");
            GoogleSignInAccount account = result.getSignInAccount();
            authenticateGoogleSignIn(account);
        } else {
            LogWrapper.info(TAG, "HandleGoogleSignInResult: Failed. Status code " + result.getStatus().getStatusCode());
            loading.postValue(false);
            showErrorToast(R.string.new_account_auth_failed);
        }
    }

    private void authenticateGoogleSignIn(GoogleSignInAccount account){
        AuthManager authManager = new FirebaseAuthManager();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        Disposable d = authManager.logInWithCredentials(credential)
                .subscribe(() -> {
                    onAuthSuccess();
                }, throwable -> {
                    loading.postValue(false);
                    showErrorToast(R.string.new_account_auth_failed);
                });

        compositeDisposable.add(d);
    }

    private void createAccount(String email, String password){
        AuthManager authManager = new FirebaseAuthManager();

        Disposable d = authManager.createNewAccount(email, password)
                .subscribe(() -> {
                    onAuthSuccess();
                }, throwable -> {
                    onCreateAccountError(throwable);
                });

        compositeDisposable.add(d);
    }

    private void onCreateAccountError(Throwable throwable) {
        loading.postValue(false);

        if (throwable instanceof FirebaseAuthWeakPasswordException){
            showErrorToast(R.string.new_account_password);
        } else if (throwable instanceof FirebaseAuthUserCollisionException){
            showErrorToast(R.string.new_account_user_collision);
        } else {
            showErrorToast(R.string.new_account_account_error);
        }
    }

    private void onAuthSuccess() {
        loading.postValue(false);
        sendIntentMainActivity.call();
    }

    private void showErrorToast(int id){
        showErrorToast.setValue(id);
    }

    private boolean validFields(String email, String password, String password2){
        if (email.isEmpty()) {
            emailError.postValue(R.string.all_required_field);
            return false;
        } else if (password.isEmpty()) {
            passwordError.postValue(R.string.all_required_field);
            return false;
        } else if (password2.isEmpty()) {
            password2Error.postValue(R.string.all_required_field);
            return false;
        }

        if (!EmailValidator.isValid(email)) {
            emailError.postValue(R.string.all_invalid_email);
            return false;
        }

        if (password.length() <= 6) {
            passwordError.postValue(R.string.all_short_password);
            return false;
        } else if (password2.length() <= 6) {
            password2Error.postValue(R.string.all_short_password);
            return false;
        }

        if (!password.equals(password2)){
            passwordError.postValue(R.string.all_passwords_dont_match);
            return false;
        }

        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }


    public MutableLiveData<Integer> getEmailError() {
        return emailError;
    }

    public MutableLiveData<Integer> getPasswordError() {
        return passwordError;
    }

    public MutableLiveData<Integer> getPassword2Error() {
        return password2Error;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public VoidSingleLiveEvent getSendIntentMainActivity() {
        return sendIntentMainActivity;
    }

    public SingleLiveEvent<Integer> getShowErrorToast() {
        return showErrorToast;
    }

    public VoidSingleLiveEvent getSendGoogleSignInIntent() {
        return sendGoogleSignInIntent;
    }
}
