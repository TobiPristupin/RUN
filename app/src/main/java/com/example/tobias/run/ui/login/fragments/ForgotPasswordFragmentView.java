package com.example.tobias.run.ui.login.fragments;

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

import com.example.tobias.run.R;
import com.example.tobias.run.ui.login.ForgotPasswordPresenter;
import com.example.tobias.run.ui.login.ForgotPasswordView;
import com.wang.avi.AVLoadingIndicatorView;

import es.dmoral.toasty.Toasty;

/**
 * Created by Tobi on 9/15/2017.
 */

public class ForgotPasswordFragmentView extends Fragment implements ForgotPasswordView {

    private static final String TAG = "LoginActivity";
    private View rootView;
    private AVLoadingIndicatorView loadingIndicator;
    private TextInputLayout emailLayout;
    private Button sendButton;
    private ForgotPasswordPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        loadingIndicator = rootView.findViewById(R.id.forgot_password_loading_indicator);
        emailLayout = rootView.findViewById(R.id.forgot_password_email);
        sendButton = rootView.findViewById(R.id.forgot_password_send_button);
        presenter = new ForgotPasswordPresenter(this);

        //Configures all TextInputLayout to remove their errors every time text is inputted
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


    /**
     * @param enabled Should be enabled to errors
     * @param error Error message to be displayed
     */
    @Override
    public void setEmailTextInputError(boolean enabled, @Nullable String error) {
        emailLayout.setErrorEnabled(enabled);
        emailLayout.setError(error);
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
        Toasty.success(getContext(), "Recovery email sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecoveryEmailFailedToast() {
        Toasty.warning(getContext(), "Email not sent. Please check your internet connection or try again").show();
    }

    @Override
    public void showTooManyRequestsToast() {
        Toasty.warning(getContext(), "Too many requests sent. Please try again").show();
    }

    //Configures all TextInputLayout to remove their errors every time text is inputted
    private void setLayoutErrorReset(){
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
