package com.example.tobias.run.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobias.run.R;
import com.example.tobias.run.app.MainActivity;
import com.example.tobias.run.utils.GoogleAuthManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.apache.commons.validator.routines.EmailValidator;

import es.dmoral.toasty.Toasty;
import mbanje.kurt.fabbutton.FabButton;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FirebaseAuth firebaseAuth;
    private FabButton fabButton;
    private static final int RC_SIGN_IN = 1516;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLayout = (TextInputLayout) findViewById(R.id.login_email);
        passwordLayout = (TextInputLayout) findViewById(R.id.login_password);
        firebaseAuth = FirebaseAuth.getInstance();
        fabButton = (FabButton) findViewById(R.id.login_button);

        initLogInButton();
        initGoogleLogIn();
        initButtons();
    }

    @Override
    public void onBackPressed() {
        //User can't exit app by pressing back.
    }

    private void initLogInButton(){
        //EditText error resets every time text is inputted
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

        passwordLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout.setError(null);
                passwordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLayout.getEditText().getText().toString().trim();
                String password = passwordLayout.getEditText().getText().toString().trim();
                EmailValidator validator = EmailValidator.getInstance();
                //Validate if email and password fields meet requirements.

                if (email.isEmpty()){
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Field is required");
                    return;
                }
                if (password.isEmpty()){
                    passwordLayout.setErrorEnabled(false);
                    passwordLayout.setError("Field is required");
                    return;
                }
                if (!validator.isValid(email)){
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Invalid email");
                    return;
                }
                if (password.length() <= 6){
                    passwordLayout.setErrorEnabled(false);
                    passwordLayout.setError("Password must be longer than 6 characters");
                    return;
                }

                startLoadingAnimation();
                //Sign in with validated data onto firebase. If successful load main activity.
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "FirebaseSignInEmail:successful");
                                    loadMainActivity();
                                } else {
                                    Log.d(TAG, "FirebaseSignInWithEmail:unsuccessful");
                                    passwordLayout.setError("Invalid credentials");
                                    stopLoadingAnimation();
                                    return;
                                }
                            }
                        });
            }
        });
    }

    private void loadMainActivity(){
        stopLoadingAnimation();
        Intent intent = new Intent(this, MainActivity.class);
        //Flags prevent user from returning to LoginActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
}

    //Initializes everything related to Google Sign In
    private void initGoogleLogIn(){
        GoogleAuthManager authManager = new GoogleAuthManager(LoginActivity.this);
        final GoogleApiClient apiClient = authManager.getApiClient(LoginActivity.this, authManager.getSignInOptions(),
                TAG);

        SignInButton googleBtn = (SignInButton) findViewById(R.id.login_google_button);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadingAnimation();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleResult(result);
        }
    }

    private void handleGoogleResult(GoogleSignInResult result){
        Log.d(TAG, "HandleGoogleSignInResultSuccess:" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthGoogleAccount(account);
        } else {
            stopLoadingAnimation();
        }
    }

    //Called to authenticate successful google log-in account into firebase to complete login flow.
    private void firebaseAuthGoogleAccount(GoogleSignInAccount account){
        Log.d(TAG, "FirebaseAuthGoogleAccount:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "FirebaseSignInWithGoogleCredential:Successful");
                    loadMainActivity();
                } else {
                    Log.w(TAG, "FirebaseSignInWithGoogleCredential:Unsuccessful " + task.getException());
                    stopLoadingAnimation();
                    Toasty.warning(LoginActivity.this, "Authentication failed. Check your internet connection or try again").show();
                }
            }
        });

    }

    private void startLoadingAnimation(){
        fabButton.showProgress(true);
    }

    private void stopLoadingAnimation(){
        fabButton.showProgress(false);
    }

    private void initButtons(){
        TextView newAccount = (TextView) findViewById(R.id.login_newaccount);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, NewAccountActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

}
