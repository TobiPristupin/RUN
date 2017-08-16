package com.example.tobias.run.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private AVLoadingIndicatorView loadingIndicator;
    private TextInputLayout emailLayout;
    private FirebaseAuth firebaseAuth;
    private Button sendButton;
    private static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.forgot_password_loading_indicator);
        emailLayout = (TextInputLayout) findViewById(R.id.forgot_password_email);
        sendButton = (Button) findViewById(R.id.forgot_password_send_button);
        firebaseAuth = firebaseAuth.getInstance();

        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

        //Configures all TextInputLayout to remove their errors every time text is inputted
        setLayoutErrorReset();
        initSendButton();
        initReturnButton();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
                    Toasty.success(ForgotPasswordActivity.this, "Recovery email sent", Toast.LENGTH_SHORT).show();
                    sendButtonStopAnim();
                } else {
                    Log.d(TAG, "FirebaseSendResetEmail:Unsuccessful " + task.getException());
                    sendButtonStopAnim();
                    if (task.getException() instanceof FirebaseTooManyRequestsException){
                        Toasty.warning(ForgotPasswordActivity.this, "Too many requests sent. Please try again").show();
                        return;
                    }
                    Toasty.warning(ForgotPasswordActivity.this, "Email not sent. Please check your internet connection or try again").show();
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
        Button returnButton = (Button) findViewById(R.id.forgot_password_back_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

}
