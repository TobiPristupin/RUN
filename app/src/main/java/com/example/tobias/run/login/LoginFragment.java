package com.example.tobias.run.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobias.run.R;
import com.example.tobias.run.app.MainActivityView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;
import mbanje.kurt.fabbutton.FabButton;

/**
 * Created by Tobi on 9/15/2017.
 */

public class LoginFragment extends Fragment implements LoginView {

    private View rootView;
    private LoginPresenter presenter;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FirebaseAuth firebaseAuth;
    private FabButton fabButton;
    private static final int RC_SIGN_IN = 1516;
    private static final String TAG = "LoginActivity";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        presenter = new LoginPresenter(this);

        emailLayout = (TextInputLayout) rootView.findViewById(R.id.login_email);
        passwordLayout = (TextInputLayout) rootView.findViewById(R.id.login_password);
        firebaseAuth = FirebaseAuth.getInstance();
        fabButton = (FabButton) rootView.findViewById(R.id.login_button);

        initLogInFab();
        initTextInputs();
        initGoogleLogIn();
        initBottomButtons();
        return rootView;
    }

    /**
     * @param enabled Should be enabled to errors
     * @param error Error message to be displayed
     */
    @Override
    public void setEmailTextInputError(boolean enabled, @Nullable String error) {
        emailLayout.setError(error);
        emailLayout.setErrorEnabled(enabled);
    }


    /**
     * @param enabled Should be enabled to errors
     * @param error Error message to be displayed
     */
    @Override
    public void setPasswordTextInputError(boolean enabled, @Nullable String error) {
        passwordLayout.setError(error);
        passwordLayout.setErrorEnabled(enabled);
    }

    @Override
    public void sendIntentMainActivity() {
        Intent intent = new Intent(getContext(), MainActivityView.class);
        //Flags prevent user from returning to LoginActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void startLoadingAnimation(){
        fabButton.showProgress(true);
    }

    @Override
    public void stopLoadingAnimation(){
        fabButton.showProgress(false);
    }

    @Override
    public void showUnexpectedLoginErrorToast() {
        Toasty.warning(getContext(), "An error has occurred. Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendGoogleSignInIntent() {
        GoogleApiClient apiClient = ((LoginActivity) getActivity()).getGoogleApiClient();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showGoogleSignInFailedToast() {
        Toasty.warning(getContext(), "Google authentication failed. Check your internet connection or try again").show();
    }

    private void initLogInFab(){
        //EditText error resets every time text is inputted
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLayout.getEditText().getText().toString().trim();
                String password = passwordLayout.getEditText().getText().toString().trim();
                presenter.attemptEmailLogin(email, password);
            }
        });
    }

    private void initTextInputs(){
        emailLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onEmailTextInputTextChanged();
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
                presenter.onPasswordTextInputTextChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    //Initializes everything related to Google Sign In
    private void initGoogleLogIn(){
        SignInButton googleBtn = (SignInButton) rootView.findViewById(R.id.login_google_button);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.attemptGoogleLogin();
            }
        });
    }

    /**
     * Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.onGoogleSignInResult(result);
        }
    }

    private void initBottomButtons(){
        TextView newAccount = (TextView) rootView.findViewById(R.id.login_newaccount);
        final ViewPager viewPager = (ViewPager) ((LoginActivity) getActivity()).findViewById(R.id.login_viewpager);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        TextView forgotPassword = (TextView) rootView.findViewById(R.id.login_forgotpassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
    }
}
