package com.example.tobias.run.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tobias.run.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.commons.validator.routines.EmailValidator;

import es.dmoral.toasty.Toasty;

/**
 * Created by Tobi on 9/15/2017.
 */

public class ForgotPasswordFragment  extends Fragment{

    private View rootView;
    private AVLoadingIndicatorView loadingIndicator;
    private TextInputLayout emailLayout;
    private FirebaseAuth firebaseAuth;
    private Button sendButton;
    private static final String TAG = "LoginActivity";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        loadingIndicator = (AVLoadingIndicatorView) rootView.findViewById(R.id.forgot_password_loading_indicator);
        emailLayout = (TextInputLayout) rootView.findViewById(R.id.forgot_password_email);
        sendButton = (Button) rootView.findViewById(R.id.forgot_password_send_button);
        firebaseAuth = firebaseAuth.getInstance();

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
                if (!emailIsValid(email)){
                    setError("Invalid email", emailLayout);
                    return;
                }
                sendButtonStartAnim();
                firebaseSendResetEmail(email);
            }
        });
    }

    private boolean emailIsValid(String email){
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    //Configures all TextInputLayout to remove their errors every time text is inputted
    private void setLayoutErrorReset(){
        emailLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailLayout.setError(null);
                emailLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setError(String message, TextInputLayout layout){
        layout.setErrorEnabled(true);
        layout.setError(message);
    }

    private void firebaseSendResetEmail(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "FirebaseSendResetEmail:Successful");
                    Toasty.success(getContext(), "Recovery email sent", Toast.LENGTH_SHORT).show();
                    sendButtonStopAnim();
                } else {
                    Log.d(TAG, "FirebaseSendResetEmail:Unsuccessful " + task.getException());
                    sendButtonStopAnim();
                    if (task.getException() instanceof FirebaseTooManyRequestsException){
                        Toasty.warning(getContext(), "Too many requests sent. Please try again").show();
                        return;
                    }
                    Toasty.warning(getContext(), "Email not sent. Please check your internet connection or try again").show();
                }
            }
        });
    }

    private void sendButtonStartAnim(){
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

    private void sendButtonStopAnim(){
        loadingIndicator.smoothToHide();
        sendButton.animate().alpha(1.0f);
    }

    private void initReturnButton(){
        Button button = (Button) rootView.findViewById(R.id.forgot_password_return);
        final ViewPager viewPager = (ViewPager) ((LoginActivity) getActivity()).findViewById(R.id.login_viewpager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }

}
