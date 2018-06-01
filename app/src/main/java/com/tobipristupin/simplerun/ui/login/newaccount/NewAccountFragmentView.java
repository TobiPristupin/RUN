package com.tobipristupin.simplerun.ui.login.newaccount;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.databinding.FragmentNewAccountBinding;
import com.tobipristupin.simplerun.ui.login.Page;
import com.tobipristupin.simplerun.ui.login.PageChanger;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.wang.avi.AVLoadingIndicatorView;

import es.dmoral.toasty.Toasty;

public class NewAccountFragmentView extends BaseLoginFragment  {

    private static final int GOOGLE_SIGN_IN_INTENT_CODE = 1379;
    private NewAccountViewModel viewModel;
    private FragmentNewAccountBinding binding;
    private PageChanger pageChanger;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordLayout2;
    private Button createAccountButton;
    private AVLoadingIndicatorView loadingIndicator;
    private ToastyWrapper toastyWrapper = new ToastyWrapper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_account, container, false);
        binding.setLifecycleOwner(this);

        emailLayout = binding.newAccountEmail;
        passwordLayout = binding.newAccountPassword1;
        passwordLayout2 = binding.newAccountPassword2;
        loadingIndicator = binding.newAccountLoadingIndicator;
        createAccountButton = binding.newAccountCreateAccountButton;

        setLayoutErrorReset(emailLayout, passwordLayout, passwordLayout2);

        setLayoutErrorReset();
        initCreateAccountButton();
        initGoogleLogIn();
        initReturnButton();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProvider.Factory factory = new NewAccountViewModel.Factory(new FirebaseAuthManager());
        viewModel = obtainViewModel(this, NewAccountViewModel.class, factory);
        bindViewModel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            pageChanger = (PageChanger) getActivity();
        } catch (ClassCastException e){
            throw new RuntimeException("Parent Activity must implement PageChanger and GoogleApiClient " +
                    "interfaces");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_INTENT_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            viewModel.onGoogleSignInResult(result);
        }
    }

    private void initCreateAccountButton() {
        createAccountButton.setOnClickListener(view -> {
            final String email = emailLayout.getEditText().getText().toString().trim();
            final String password = passwordLayout.getEditText().getText().toString().trim();
            final String password2 = passwordLayout2.getEditText().getText().toString().trim();

            viewModel.onCreateAccountClick(email, password, password2);
        });

    }

    private void initGoogleLogIn(){
        binding.newAccountGoogleButton.setOnClickListener(view -> viewModel.onGoogleLogInClick());
    }

    private void initReturnButton(){
        binding.newAccountReturn.setOnClickListener(view -> pageChanger.changeTo(Page.LOGIN));
    }

    private void bindViewModel() {
        bindEditTextErrors();
        bindMainActivityIntent();
        bindLoading();
        bindToasts();
        bindGoogleSignInIntent();
    }

    private void bindGoogleSignInIntent() {
        viewModel.getSendGoogleSignInIntent().observe(this, aVoid -> {
            GoogleApiClient apiClient = ((LoginActivity) getActivity()).getClient();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_INTENT_CODE);
        });
    }

    private void bindToasts() {
        viewModel.getShowErrorToast().observe(this, (resId) -> {
            toastyWrapper.show(Toasty.warning(getContext(), getString(resId)));
        });
    }

    private void bindLoading() {
        viewModel.getLoading().observe(this, (enable) -> {
            if (enable){
                startLoadingAnimation();
            } else {
                stopLoadingAnimation();
            }
        });
    }

    private void bindMainActivityIntent() {
        viewModel.getSendIntentMainActivity().observe(this, (aVoid -> {
            sendMainActivityIntent();
        }));
    }

    private void bindEditTextErrors() {
        viewModel.getEmailError().observe(this, (resId) -> {
            setEditTextError(emailLayout, resId);
        });

        viewModel.getPasswordError().observe(this, (resId) -> {
            setEditTextError(passwordLayout, resId);
        });

        viewModel.getPassword2Error().observe(this, (resId) -> {
            setEditTextError(passwordLayout2, resId);
        });
    }

    private void setEditTextError(TextInputLayout editText, Integer resId) {
        editText.setErrorEnabled(true);
        editText.setError(getString(resId));
    }

    public void startLoadingAnimation() {
        //Fade out
        createAccountButton.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //When button ends fading animation, start loading animation
                loadingIndicator.smoothToShow();
            }
        });
    }

    public void stopLoadingAnimation() {
        //Fade in
        loadingIndicator.smoothToHide();
        createAccountButton.animate().alpha(1.0f);
    }
}
