package com.tobipristupin.simplerun.ui.login.loginview;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.databinding.FragmentLoginBinding;
import com.tobipristupin.simplerun.ui.login.GoogleApiClientProvider;
import com.tobipristupin.simplerun.ui.login.Page;
import com.tobipristupin.simplerun.ui.login.PageChanger;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.tobipristupin.simplerun.ui.main.MainActivityView;

import mbanje.kurt.fabbutton.FabButton;


public class LoginFragmentView extends BaseLoginFragment {

    private static final int RC_SIGN_IN = 1516;
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private GoogleApiClientProvider googleApiClientProvider;
    private PageChanger pageChanger;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FabButton fabButton;
    private ToastyWrapper loginErrorToast = new ToastyWrapper();
    private ToastyWrapper googleSignInFailedToast = new ToastyWrapper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.setModel(viewModel);

        emailLayout = binding.loginEmail;
        passwordLayout = binding.loginPassword;
        fabButton = binding.loginButton;

        setLayoutErrorReset(emailLayout, passwordLayout);

        initLogInFab();
        initGoogleLogIn();
        initBottomButtons();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = obtainViewModel(this, LoginViewModel.class);
        bindViewModel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            pageChanger = (PageChanger) getActivity();
            googleApiClientProvider = (GoogleApiClientProvider) getActivity();
        } catch (ClassCastException e){
            throw new RuntimeException("Parent Activity must implement PageChanger and GoogleApiClient " +
                    "interfaces");
        }
    }

    private void bindViewModel(){
        bindMainActivityIntent();
        bindLoadingAnimation();
        bindGoogleSignInIntentAction();
        bindErrorToasts();
        bindErrorMessages();
        bindChangePageActions();
    }

    private void bindChangePageActions() {
        viewModel.getOpenForgotPasswordPageAction().observe(this, (aVoid) -> {
            pageChanger.changeTo(Page.FORGOT_PASSWORD);
        });
        viewModel.getOpenNewAccountPageAction().observe(this, (aVoid) -> {
            pageChanger.changeTo(Page.NEW_ACCOUNT);
        });
    }

    private void bindMainActivityIntent(){
        viewModel.getSendIntentToMainActivityAction().observe(this, (aVoid) -> {
            Intent intent = new Intent(getContext(), MainActivityView.class);
            //Flags prevent user from returning to LoginActivity when pressing back button
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void bindLoadingAnimation(){
        //No way to start progress through xml properties, so no databinding
        viewModel.getShowLoadingAnimation().observe(this, aBoolean -> {
            fabButton.showProgress(aBoolean);
        });
    }

    private void bindGoogleSignInIntentAction(){
        viewModel.getGoogleSignInIntentAction().observe(this, (aVoid) -> {
            GoogleApiClient apiClient = googleApiClientProvider.getClient();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void bindErrorToasts(){
        viewModel.getShowLoginErrorToast().observe(this, (aVoid) -> {
            String str = getString(R.string.login_fragment_view_errortoast);
            loginErrorToast.showWarning(getContext(), str, Toast.LENGTH_SHORT);
        });
        viewModel.getShowGoogleSignInErrorToastAction().observe(this, (aVoid) -> {
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



    private void initLogInFab(){
        fabButton.setOnClickListener(view -> {
            String email = emailLayout.getEditText().getText().toString().trim();
            String password = passwordLayout.getEditText().getText().toString().trim();
            viewModel.attemptEmailLogin(email, password);
        });
    }

    //Initializes everything related to Google Sign In
    private void initGoogleLogIn(){
        binding.loginGoogleButton.setOnClickListener(v -> viewModel.attemptGoogleLogIn());
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
        binding.loginNewaccount.setOnClickListener(view -> viewModel.onNewAccountClicked());
        binding.loginForgotpassword.setOnClickListener(view -> viewModel.onForgotPasswordClicked());
    }
}
