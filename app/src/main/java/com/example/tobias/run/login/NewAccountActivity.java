package com.example.tobias.run.login;

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

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;

public class NewAccountActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordLayout2;
    private FirebaseAuth firebaseAuth;
    private CircularProgressButton createAccountButton;
    private static final String TAG = "NewAccountActivity";
    private static final int RC_SIGN_IN = 1379;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        emailLayout = (TextInputLayout) findViewById(R.id.new_account_email);
        passwordLayout = (TextInputLayout) findViewById(R.id.new_account_password1);
        passwordLayout2 = (TextInputLayout) findViewById(R.id.new_account_password2);
        firebaseAuth = FirebaseAuth.getInstance();
        createAccountButton = (CircularProgressButton) findViewById(R.id.new_account_create_account_button);

        initCreateAccountButton();
        initGoogleLogIn();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        createAccountButton.dispose();
    }

    private void initCreateAccountButton() {
        //Configures all TextInputLayout to remove their errors every time text is inputted
        setLayoutErrorReset();
        //Button createAccountBtn = (Button) findViewById(R.id.new_account_create_account_button);
        CircularProgressButton createAccountBtn = (CircularProgressButton) findViewById(R.id.new_account_create_account_button);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailLayout.getEditText().getText().toString().trim();
                final String password = passwordLayout.getEditText().getText().toString().trim();
                final String password2 = passwordLayout2.getEditText().getText().toString().trim();

                //Validates if submitted email and password fields meet requirements
                if (validateFields(email, password, password2)){
                    //Matching passwords have been validated, no need to pass both.
                    firebaseCreateAccount(email, password);
              }

            }
        });

    }

    //Configures all TextInputLayout to remove their errors every time text is inputted
    private void setLayoutErrorReset() {
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

        passwordLayout2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout2.setError(null);
                passwordLayout2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setError(String errorMsg, TextInputLayout view) {
        view.setErrorEnabled(true);
        view.setError(errorMsg);
    }

    //Validates if submitted email and password fields meet requirements
    private boolean validateFields(String email, String password, String password2) {

        EmailValidator validator = EmailValidator.getInstance();

        if (email.isEmpty()) {
            setError("Field is required", emailLayout);
            return false;
        } else if (password.isEmpty()) {
            setError("Field is required", passwordLayout);
            return false;
        } else if (password2.isEmpty()) {
            setError("Field is required", passwordLayout2);
            return false;
        }

        if (!validator.isValid(email)) {
            setError("Invalid email", emailLayout);
            return false;
        }

        if (password.length() <= 6) {
            setError("Password must be longer than 6 characters", passwordLayout);
            return false;
        } else if (password2.length() <= 6) {
            setError("Password must be longer than 6 characters", passwordLayout2);
            return false;
        }

        if (!password.equals(password2)){
            setError("Passwords do not match", passwordLayout);
            return false;
        }

    return true;

    }

    private void firebaseCreateAccount(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(NewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "FirebaseCreateUserWithEmail:Success");
                            loadMainActivity();
                        } else {
                            Log.d(TAG, "FirebaseCreateUserWithEmail:Unsuccessful " + task.getException());
                            Toasty.warning(NewAccountActivity.this, "Unable to create new account").show();
                        }
                    }
                });
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        //Flags prevent user from returning to LoginActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Initializes everything related to Google Sign In
    private void initGoogleLogIn(){
        GoogleAuthManager googleAuthManager = new GoogleAuthManager(NewAccountActivity.this);
        final GoogleApiClient apiClient = googleAuthManager.getApiClient(NewAccountActivity.this, googleAuthManager.getSignInOptions(),
                TAG);

        SignInButton signInButton = (SignInButton) findViewById(R.id.new_account_google_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountButton.startAnimation();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleResult(result);
        }
    }

    private void handleGoogleResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthGoogleAccount(account);
        } else {
            Toasty.warning(NewAccountActivity.this, "Google sign in failed. Check your internet connection or try again").show();
            createAccountButton.stopAnimation();
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
                            Toasty.warning(NewAccountActivity.this, "Authentication failed. Check your internet connection or try again").show();
                            createAccountButton.stopAnimation();
                        }
                    }
                });
    }
}
