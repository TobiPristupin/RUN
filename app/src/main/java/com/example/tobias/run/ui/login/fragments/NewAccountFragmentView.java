package com.example.tobias.run.ui.login.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.Button;

import com.example.tobias.run.R;
import com.example.tobias.run.ui.login.LoginActivity;
import com.example.tobias.run.ui.login.NewAccountPresenter;
import com.example.tobias.run.ui.login.NewAccountView;
import com.example.tobias.run.ui.main.MainActivityView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wang.avi.AVLoadingIndicatorView;

import es.dmoral.toasty.Toasty;

/**
 * Created by Tobi on 9/15/2017.
 */

public class NewAccountFragmentView extends Fragment implements NewAccountView {

    private static final int RC_SIGN_IN = 1379;
    private View rootView;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordLayout2;
    private Button createAccountButton;
    private AVLoadingIndicatorView loadingIndicator;
    private NewAccountPresenter presenter;

    @Override
    public void setEmailTextInputError(boolean enabled, @Nullable String error) {
        emailLayout.setErrorEnabled(enabled);
        emailLayout.setError(error);
    }

    @Override
    public void setPasswordTextInputError(boolean enabled, @Nullable String error) {
        passwordLayout.setErrorEnabled(enabled);
        passwordLayout.setError(error);
    }

    @Override
    public void setPassword2TextInputError(boolean enabled, @Nullable String error) {
        passwordLayout2.setErrorEnabled(enabled);
        passwordLayout2.setError(error);
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
    public void startLoadingAnimation() {
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

    @Override
    public void stopLoadingAnimation() {
        //Fade in
        loadingIndicator.smoothToHide();
        createAccountButton.animate().alpha(1.0f);
    }

    @Override
    public void showCreateAccountErrorToast() {
        Toasty.warning(getContext(), "Unable to create new account").show();
    }

    @Override
    public void showWeakPasswordErrorToast() {
        Toasty.warning(getContext(), "Password is not strong enough").show();
    }

    @Override
    public void showUserCollisionToast() {
        Toasty.warning(getContext(), "User with same email already exists").show();
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

    private void initCreateAccountButton() {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailLayout.getEditText().getText().toString().trim();
                final String password = passwordLayout.getEditText().getText().toString().trim();
                final String password2 = passwordLayout2.getEditText().getText().toString().trim();

                presenter.onCreateAccountButtonClick(email, password, password2);
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

        passwordLayout2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onPassword2TextInputTextChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initGoogleLogIn(){
        SignInButton signInButton = rootView.findViewById(R.id.new_account_google_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGoogleLogInClick();
            }
        });

    }

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.onGoogleSignInResult(result);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_account, container, false);

        emailLayout = rootView.findViewById(R.id.new_account_email);
        passwordLayout = rootView.findViewById(R.id.new_account_password1);
        passwordLayout2 = rootView.findViewById(R.id.new_account_password2);
        loadingIndicator = rootView.findViewById(R.id.new_account_loading_indicator);
        createAccountButton = rootView.findViewById(R.id.new_account_create_account_button);

        presenter = new NewAccountPresenter(this);

        //Configures all TextInputLayout to remove their errors every time text is inputted
        setLayoutErrorReset();
        initCreateAccountButton();
        initGoogleLogIn();
        initReturnButton();

        return rootView;
    }

    private void initReturnButton(){
        Button button = rootView.findViewById(R.id.new_account_return);
        final ViewPager viewPager = getActivity().findViewById(R.id.login_viewpager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }
}
