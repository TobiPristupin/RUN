package com.tobipristupin.simplerun.ui.login.forgotpassword;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.Toast;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.ui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;
import com.wang.avi.AVLoadingIndicatorView;

import es.dmoral.toasty.Toasty;


public class ForgotPasswordFragmentView extends BaseLoginFragment implements ForgotPasswordView {

    private static final String TAG = "LoginActivity";
    private View rootView;
    private AVLoadingIndicatorView loadingIndicator;
    private TextInputLayout emailLayout;
    private Button sendButton;
    private ForgotPasswordPresenter presenter;

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
        presenter = new ForgotPasswordPresenter(this);

        setLayoutErrorReset(emailLayout);

        setLayoutErrorReset();
        initSendButton();
        initReturnButton();

        return rootView;
    }

    private void initSendButton(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if email is valid before starting loading animation
                String email = emailLayout.getEditText().getText().toString().trim();
                presenter.onSendEmailButtonClicked(email);
            }
        });
    }

    @Override
    public void enableEmailError() {
        emailLayout.setErrorEnabled(true);
        emailLayout.setError(getString(R.string.all_invalid_email));
    }

    @Override
    public void startLoadingAnimation() {
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

    @Override
    public void stopLoadingAnimation() {
        loadingIndicator.smoothToHide();
        sendButton.animate().alpha(1.0f);
    }

    @Override
    public void showRecoveryEmailSentToast() {
        String str = getString(R.string.forgot_password_fragment_view_recoverytoast);
        recoveryEmailSent.showSuccess(getContext(), str, Toast.LENGTH_SHORT);
    }

    @Override
    public void showRecoveryEmailFailedToast() {
        String str = getString(R.string.forgot_password_fragment_view_recovery_failedtoast);
        recoveryEmailFailed.showWarning(getContext(), str, Toast.LENGTH_SHORT);
    }

    @Override
    public void showTooManyRequestsToast() {
        String str = getString(R.string.forgot_password_fragment_view_toomanyrequests_toast);
        tooManyRequestsToast.showWarning(getContext(), str);
    }

    private void initReturnButton(){
        Button button = rootView.findViewById(R.id.forgot_password_return);
        final ViewPager viewPager = getActivity().findViewById(R.id.login_viewpager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }

}
