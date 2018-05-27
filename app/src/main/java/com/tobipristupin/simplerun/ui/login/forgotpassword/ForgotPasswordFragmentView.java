package com.tobipristupin.simplerun.ui.login.forgotpassword;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.ui.login.Page;
import com.tobipristupin.simplerun.ui.login.PageChanger;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;
import com.wang.avi.AVLoadingIndicatorView;


public class ForgotPasswordFragmentView extends BaseLoginFragment {

    private View rootView;
    private AVLoadingIndicatorView loadingIndicator;
    private TextInputLayout emailLayout;
    private Button sendButton;
    private ForgotPasswordViewModel viewModel;
    private PageChanger pageChanger;
    private ToastyWrapper recoveryEmailSent = new ToastyWrapper();
    private ToastyWrapper recoveryEmailFailed = new ToastyWrapper();
    private ToastyWrapper tooManyRequestsToast = new ToastyWrapper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        loadingIndicator = rootView.findViewById(R.id.forgot_password_loading_indicator);
        emailLayout = rootView.findViewById(R.id.forgot_password_email);
        sendButton = rootView.findViewById(R.id.forgot_password_send_button);

        setLayoutErrorReset(emailLayout);
        initSendButton();
        initReturnButton();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = obtainViewModel(this, ForgotPasswordViewModel.class);
        bindViewModel();
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

    private void initSendButton(){
        sendButton.setOnClickListener(view -> {
            String email = emailLayout.getEditText().getText().toString().trim();
            viewModel.onSendEmailButtonClick(email);
        });
    }

    private void bindViewModel(){
        bindEmailError();
        bindLoading();
        bindToasts();
        bindOpenLoginPage();
    }

    private void bindEmailError(){
        viewModel.getEmailError().observe(this, error -> {
            if (error == ErrorType.EmailLogin.INVALID_EMAIL){
                emailLayout.setError(getString(R.string.all_invalid_email));
            } else {
                emailLayout.setError(getString(R.string.all_error));
            }
        });
    }

    private void bindLoading(){
        viewModel.getLoading().observe(this, state -> {
            if (state){
                startLoadingAnimation();
            } else {
                stopLoadingAnimation();
            }
        });
    }

    private void bindToasts(){
        viewModel.getShowEmailSentToast().observe(this, aVoid -> {
            String str = getString(R.string.forgot_password_fragment_view_recoverytoast);
            recoveryEmailSent.showSuccess(getContext(), str, Toast.LENGTH_SHORT);
        });

        viewModel.getShowEmailFailedToast().observe(this, aVoid -> {
            String str = getString(R.string.forgot_password_fragment_view_recovery_failedtoast);
            recoveryEmailFailed.showWarning(getContext(), str, Toast.LENGTH_SHORT);
        });

        viewModel.getShowTooManyRequestsToast().observe(this, aVoid -> {
            String str = getString(R.string.forgot_password_fragment_view_toomanyrequests_toast);
            tooManyRequestsToast.showWarning(getContext(), str);
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

    private void initReturnButton(){
        Button button = rootView.findViewById(R.id.forgot_password_return);
        button.setOnClickListener(view -> viewModel.onReturnButtonClick());
    }

}
