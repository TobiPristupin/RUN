package com.tobipristupin.simplerun.ui.login.forgotpassword;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.auth.FirebaseAuthManager;
import com.tobipristupin.simplerun.databinding.FragmentForgotPasswordBinding;
import com.tobipristupin.simplerun.ui.login.Page;
import com.tobipristupin.simplerun.ui.login.PageChanger;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;
import com.wang.avi.AVLoadingIndicatorView;

import es.dmoral.toasty.Toasty;


public class ForgotPasswordFragmentView extends BaseLoginFragment {

    private FragmentForgotPasswordBinding binding;
    private AVLoadingIndicatorView loadingIndicator;
    private TextInputLayout emailLayout;
    private Button sendButton;
    private ForgotPasswordViewModel viewModel;
    private PageChanger pageChanger;
    private ToastyWrapper toastyWrapper = new ToastyWrapper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        binding.setLifecycleOwner(this);

        loadingIndicator = binding.forgotPasswordLoadingIndicator;
        emailLayout = binding.forgotPasswordEmail;
        sendButton = binding.forgotPasswordSendButton;

        setLayoutErrorReset(emailLayout);
        initSendButton();
        initReturnButton();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProvider.Factory factory = new ForgotPasswordViewModel.Factory(new FirebaseAuthManager());
        viewModel = obtainViewModel(this, ForgotPasswordViewModel.class, factory);
        subscribeUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            pageChanger = (PageChanger) getActivity();
        } catch (ClassCastException e){
            throw new RuntimeException("Parent Activity must implement PageChanger interface");
        }
    }

    private void initReturnButton(){
        binding.forgotPasswordReturn.setOnClickListener(view -> viewModel.onReturnButtonClick());
    }

    private void initSendButton(){
        sendButton.setOnClickListener(view -> {
            String email = emailLayout.getEditText().getText().toString().trim();
            viewModel.onSendEmailButtonClick(email);
        });
    }

    private void subscribeUI(){
        bindEmailError();
        bindLoading();
        bindToasts();
        bindOpenLoginPage();
    }

    private void bindEmailError(){
        viewModel.getEmailError().observe(this, resId -> {
            emailLayout.setError(getString(resId));
        });
    }

    private void bindLoading(){
        viewModel.getLoading().observe(this, enable -> {
            if (enable){
                startLoadingAnimation();
            } else {
                stopLoadingAnimation();
            }
        });
    }

    private void bindToasts(){
        viewModel.getShowErrorToast().observe(this, resId ->  {
            toastyWrapper.show(Toasty.warning(getContext(), getString(resId)));
        });

        viewModel.getShowSuccessToast().observe(this, resId ->  {
            toastyWrapper.show(Toasty.success(getContext(), getString(resId)));
        });
    }

    private void bindOpenLoginPage(){
        viewModel.getOpenLoginPage().observe(this, aVoid -> {
            pageChanger.changeTo(Page.LOGIN);
        });
    }

    private void startLoadingAnimation() {
        //Fade out animation
        sendButton.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //When button ends fading animation, start loading animation
                loadingIndicator.smoothToShow();
            }
        });
    }

    private void stopLoadingAnimation() {
        loadingIndicator.smoothToHide();
        sendButton.animate().alpha(1.0f);
    }

}
