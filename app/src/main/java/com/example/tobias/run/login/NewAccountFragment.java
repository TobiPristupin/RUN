package com.example.tobias.run.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tobias.run.R;
import com.example.tobias.run.app.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.commons.validator.routines.EmailValidator;

import es.dmoral.toasty.Toasty;

/**
 * Created by Tobi on 9/15/2017.
 */

public class NewAccountFragment extends Fragment {

    private View rootView;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordLayout2;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Button createAccountButton;
    private AVLoadingIndicatorView loadingIndicator;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1379;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_account, container, false);

        emailLayout = (TextInputLayout) rootView.findViewById(R.id.new_account_email);
        passwordLayout = (TextInputLayout) rootView.findViewById(R.id.new_account_password1);
        passwordLayout2 = (TextInputLayout) rootView.findViewById(R.id.new_account_password2);
        loadingIndicator = (AVLoadingIndicatorView) rootView.findViewById(R.id.new_account_loading_indicator);
        createAccountButton = (Button) rootView.findViewById(R.id.new_account_create_account_button);

        //Configures all TextInputLayout to remove their errors every time text is inputted
        setLayoutErrorReset();
        initCreateAccountButton();
        initGoogleLogIn();
        initReturnButton();

        return rootView;
    }


    private void initCreateAccountButton() {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailLayout.getEditText().getText().toString().trim();
                final String password = passwordLayout.getEditText().getText().toString().trim();
                final String password2 = passwordLayout2.getEditText().getText().toString().trim();

                //Validates if submitted email and password fields meet requirements
                if (validateFields(email, password, password2)){
                    //Matching passwords have been validated, no need to pass both.
                    firebaseCreateAccount(email, password);
                }

            }
        });

    }

    //Configures all TextInputLayout to remove their errors every time text is inputted
    private void setLayoutErrorReset() {
        emailLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailLayout.setError(null);
                emailLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout.setError(null);
                passwordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordLayout2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout2.setError(null);
                passwordLayout2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setError(String errorMsg, TextInputLayout view) {
        view.setErrorEnabled(true);
        view.setError(errorMsg);
    }

    //Validates if submitted email and password fields meet requirements
    private boolean validateFields(String email, String password, String password2) {

        EmailValidator validator = EmailValidator.getInstance();

        if (email.isEmpty()) {
            setError("Field is required", emailLayout);
            return false;
        } else if (password.isEmpty()) {
            setError("Field is required", passwordLayout);
            return false;
        } else if (password2.isEmpty()) {
            setError("Field is required", passwordLayout2);
            return false;
        }

        if (!validator.isValid(email)) {
            setError("Invalid email", emailLayout);
            return false;
        }

        if (password.length() <= 6) {
            setError("Password must be longer than 6 characters", passwordLayout);
            return false;
        } else if (password2.length() <= 6) {
            setError("Password must be longer than 6 characters", passwordLayout2);
            return false;
        }

        if (!password.equals(password2)){
            setError("Passwords do not match", passwordLayout);
            return false;
        }

        return true;

    }

    private void firebaseCreateAccount(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "FirebaseCreateUserWithEmail:Success");
                            loadMainActivity();
                        } else {
                            Log.d(TAG, "FirebaseCreateUserWithEmail:Unsuccessful " + task.getException());
                            Toasty.warning(getContext(), "Unable to create new account").show();
                        }
                    }
                });
    }

    private void loadMainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        //Flags prevent user from returning to LoginActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Initializes everything related to Google Sign In
    private void initGoogleLogIn(){
        final GoogleApiClient apiClient = ((LoginActivity) getActivity()).getGoogleApiClient();
        SignInButton signInButton = (SignInButton) rootView.findViewById(R.id.new_account_google_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoadingAnim();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleResult(result);
        }
    }

    private void handleGoogleResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthGoogleAccount(account);
        } else {
            Toasty.warning(getContext(), "Google sign in failed. Check your internet connection or try again").show();
            endLoadingAnim();
        }
    }

    //Called to authenticate successful google log-in account into firebase to complete login flow.
    private void firebaseAuthGoogleAccount(GoogleSignInAccount account){
        Log.d(TAG, "FirebaseAuthGoogleAccount:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "FirebaseSignInWithGoogleCredential:Successful");
                            loadMainActivity();
                        } else {
                            Log.w(TAG, "FirebaseSignInWithGoogleCredential:Unsuccessful " + task.getException());
                            Toasty.warning(getContext(), "Authentication failed. Check your internet connection or try again").show();
                            endLoadingAnim();
                        }
                    }
                });
    }

    private void startLoadingAnim(){
        //Fade out animation
        createAccountButton.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //When button ends fading animation, start loading animation
                loadingIndicator.smoothToShow();
            }
        });
    }

    private void endLoadingAnim(){
        //Fade in
        loadingIndicator.smoothToHide();
        createAccountButton.animate().alpha(1.0f);
    }

    private void initReturnButton(){
        Button button = (Button) rootView.findViewById(R.id.new_account_return);
        final ViewPager viewPager = (ViewPager) ((LoginActivity) getActivity()).findViewById(R.id.login_viewpager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }
}
