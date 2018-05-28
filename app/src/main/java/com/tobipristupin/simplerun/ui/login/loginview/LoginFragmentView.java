package com.tobipristupin.simplerun.ui.login.loginview;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.databinding.FragmentLoginBinding;
import com.tobipristupin.simplerun.ui.login.GoogleApiClientProvider;
import com.tobipristupin.simplerun.ui.login.Page;
import com.tobipristupin.simplerun.ui.login.PageChanger;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;

import es.dmoral.toasty.Toasty;
import mbanje.kurt.fabbutton.FabButton;


public class LoginFragmentView extends BaseLoginFragment {

    private static final int GOOGLE_SIGN_IN_INTENT_CODE = 1516;
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private GoogleApiClientProvider googleApiClientProvider;
    private PageChanger pageChanger;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FabButton fabButton;
    private ToastyWrapper toastyWrapper = new ToastyWrapper();

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
        ViewModelProvider.Factory factory = new LoginViewModel.Factory(new FirebaseAuthManager());
        viewModel = obtainViewModel(this, LoginViewModel.class, factory);
        subscribeUI();
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

    private void subscribeUI(){
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
            sendMainActivityIntent();
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
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_INTENT_CODE);
        });
    }

    private void bindErrorToasts(){
        viewModel.getShowErrorToast().observe(this, (resId) -> {
            toastyWrapper.show(Toasty.warning(getContext(), getString(resId)));
        });
    }

    private void bindErrorMessages(){
        viewModel.getEmailError().observe(this, (resId) -> {
            emailLayout.setError(getString(resId));
        });

        viewModel.getPasswordError().observe(this, (resId) -> {
            passwordLayout.setError(getString(resId));
        });
    }



    private void initLogInFab(){
        fabButton.setOnClickListener(view -> {
            String email = emailLayout.getEditText().getText().toString().trim();
            String password = passwordLayout.getEditText().getText().toString().trim();
            viewModel.attemptEmailLogin(email, password);
        });
    }

    private void initGoogleLogIn(){
        binding.loginGoogleButton.setOnClickListener(v -> viewModel.attemptGoogleLogIn());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_INTENT_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            viewModel.onGoogleSignInResult(result);
        }
    }

    private void initBottomButtons(){
        binding.loginNewaccount.setOnClickListener(view -> viewModel.onNewAccountClicked());
        binding.loginForgotpassword.setOnClickListener(view -> viewModel.onForgotPasswordClicked());
    }
}
