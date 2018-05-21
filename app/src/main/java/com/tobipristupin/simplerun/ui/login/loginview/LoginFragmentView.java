package com.tobipristupin.simplerun.ui.login.loginview;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.tobipristupin.simplerun.ui.main.MainActivityView;

import mbanje.kurt.fabbutton.FabButton;


public class LoginFragmentView extends BaseLoginFragment {

    private static final int RC_SIGN_IN = 1516;
    private View rootView;
    private LoginViewModel viewModel;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FabButton fabButton;
    private ToastyWrapper loginErrorToast = new ToastyWrapper();
    private ToastyWrapper googleSignInFailedToast = new ToastyWrapper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        emailLayout = rootView.findViewById(R.id.login_email);
        passwordLayout = rootView.findViewById(R.id.login_password);
        fabButton = rootView.findViewById(R.id.login_button);

        setLayoutErrorReset(emailLayout, passwordLayout);

        initLogInFab();
        initGoogleLogIn();
        initBottomButtons();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        bindViewModel();
    }

    private void bindViewModel(){
        bindMainActivityIntent();
        bindLoadingAnimation();
        bindGoogleSignInIntentAction();
        bindErrorToasts();
        bindErrorMessages();
        bindViewPagerPosition();
    }

    private void bindMainActivityIntent(){
        viewModel.getSendIntentToMainActivityAction().observe(this, (nothing) -> {
            Intent intent = new Intent(getContext(), MainActivityView.class);
            //Flags prevent user from returning to LoginActivity when pressing back button
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void bindLoadingAnimation(){
        viewModel.getShowLoadingAnimation().observe(this, (state) -> {
            fabButton.showProgress(state);
        });
    }

    private void bindGoogleSignInIntentAction(){
        viewModel.getGoogleSignInIntentAction().observe(this, (nothing) -> {
            GoogleApiClient apiClient = ((LoginActivity) getActivity()).getGoogleApiClient();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void bindErrorToasts(){
        viewModel.getShowLoginErrorToast().observe(this, (nothing) -> {
            String str = getString(R.string.login_fragment_view_errortoast);
            loginErrorToast.showWarning(getContext(), str, Toast.LENGTH_SHORT);
        });
        viewModel.getShowGoogleSignInErrorToastAction().observe(this, (nothing) -> {
            String str = getString(R.string.login_fragment_view_google_toast);
            googleSignInFailedToast.showWarning(getContext(), str);
        });
    }

    private void bindErrorMessages(){
        viewModel.getEmailError().observe(this, (error) -> {
            emailLayout.setErrorEnabled(true);

            switch (error) {
                case INVALID_EMAIL :
                    emailLayout.setError(getString(R.string.all_invalid_email));
                    break;
                case REQUIRED_FIELD :
                    emailLayout.setError(getString(R.string.all_required_field));
                    break;
                case USERNAME_DOESNT_EXIST :
                    emailLayout.setError(getString(R.string.all_username_doesnt_exist));
                    break;
                default :
                    emailLayout.setError(getString(R.string.all_error));
            }
        });

        viewModel.getPasswordError().observe(this, (error) -> {
            switch (error) {
                case REQUIRED_FIELD :
                    passwordLayout.setError(getString(R.string.all_required_field));
                    break;
                case SHORT_PASSWORD :
                    passwordLayout.setError(getString(R.string.all_short_password));
                    break;
                case INVALID_CREDENTIALS :
                    passwordLayout.setError(getString(R.string.all_invalid_credentials));
                    break;
                default :
                    passwordLayout.setError(getString(R.string.all_error));
            }
        });
    }

    private void bindViewPagerPosition(){
        viewModel.getViewPagerPosition().observe(this, (position) -> {
            final ViewPager viewPager = getActivity().findViewById(R.id.login_viewpager);
            viewPager.setCurrentItem(position);
        });
    }

    private void initLogInFab(){
        fabButton.setOnClickListener(view -> {
            String email = emailLayout.getEditText().getText().toString().trim();
            String password = passwordLayout.getEditText().getText().toString().trim();
            viewModel.attemptEmailLogin(email, password);
        });
    }

    //Initializes everything related to Google Sign In
    private void initGoogleLogIn(){
        SignInButton googleBtn = rootView.findViewById(R.id.login_google_button);
        googleBtn.setOnClickListener(v -> viewModel.attemptGoogleLogIn());
    }

    /**
     * Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            viewModel.onGoogleSignInResult(result);
        }
    }

    private void initBottomButtons(){
        TextView newAccount = rootView.findViewById(R.id.login_newaccount);
        newAccount.setOnClickListener(view -> viewModel.onNewAccountClicked());

        TextView forgotPassword = rootView.findViewById(R.id.login_forgotpassword);
        forgotPassword.setOnClickListener(view -> viewModel.onForgotPasswordClicked());
    }
}
