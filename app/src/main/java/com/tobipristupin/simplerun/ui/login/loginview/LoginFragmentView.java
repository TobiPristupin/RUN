package com.tobipristupin.simplerun.ui.login.loginview;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.tobipristupin.simplerun.ui.main.MainActivityView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import es.dmoral.toasty.Toasty;
import mbanje.kurt.fabbutton.FabButton;

/**
 * Created by Tobi on 9/15/2017.
 */

public class LoginFragmentView extends Fragment implements LoginView {

    private static final int RC_SIGN_IN = 1516;
    private View rootView;
    private LoginPresenter presenter;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FabButton fabButton;
    private Toast loginErrorToast;
    private Toast googleSignInFailedToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        presenter = new LoginPresenter(this);

        emailLayout = rootView.findViewById(R.id.login_email);
        passwordLayout = rootView.findViewById(R.id.login_password);
        fabButton = rootView.findViewById(R.id.login_button);

        initLogInFab();
        setTextInputErrorReset();
        initGoogleLogIn();
        initBottomButtons();
        return rootView;
    }

    @Override
    public void enableEmailError(ErrorType.EmailLogin type) {
        emailLayout.setErrorEnabled(true);

        switch (type) {
            case INVALID_EMAIL :
                emailLayout.setError(getString(R.string.all_invalid_email));
                break;
            case REQUIRED_FIELD :
                emailLayout.setError(getString(R.string.all_required_field));
                break;
            default :
                emailLayout.setError(getString(R.string.all_error));
        }
    }

    @Override
    public void disableEmailError() {
        emailLayout.setErrorEnabled(false);
    }

    @Override
    public void enablePasswordError(ErrorType.PasswordLogin type) {
        passwordLayout.setErrorEnabled(true);

        switch (type) {
            case REQUIRED_FIELD :
                passwordLayout.setError(getString(R.string.all_required_field));
                break;
            case SHORT_PASSWORD :
                passwordLayout.setError(getString(R.string.all_short_password));
                break;
            case INVALID_CREDENTIALS :
                passwordLayout.setError(getString(R.string.all_invalid_credentials));
                break;
            default :
                passwordLayout.setError(getString(R.string.all_error));
        }
    }

    @Override
    public void disablePasswordError() {
        passwordLayout.setErrorEnabled(false);
    }

    @Override
    public void startLoadingAnimation() {
        fabButton.showProgress(true);
    }

    @Override
    public void stopLoadingAnimation() {
        fabButton.showProgress(false);
    }

    @Override
    public void sendIntentMainActivity() {
        Intent intent = new Intent(getContext(), MainActivityView.class);
        //Flags prevent user from returning to LoginActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showUnexpectedLoginErrorToast() {
        if (loginErrorToast != null){
            loginErrorToast.cancel();
        }

        String str = getString(R.string.login_fragment_view_errortoast);

        loginErrorToast = Toasty.warning(getContext(), str, Toast.LENGTH_SHORT);
        loginErrorToast.show();
    }

    @Override
    public void sendGoogleSignInIntent() {
        GoogleApiClient apiClient = ((LoginActivity) getActivity()).getGoogleApiClient();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showGoogleSignInFailedToast() {
        if (googleSignInFailedToast != null){
            googleSignInFailedToast.cancel();
        }

        String str = getString(R.string.login_fragment_view_google_toast);

        googleSignInFailedToast = Toasty.warning(getContext(), str);

        googleSignInFailedToast.show();
    }

    private void initLogInFab(){
        //EditText error resets every time text is inputted
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLayout.getEditText().getText().toString().trim();
                String password = passwordLayout.getEditText().getText().toString().trim();
                presenter.attemptEmailLogin(email, password);
            }
        });
    }

    private void setTextInputErrorReset(){
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

        passwordLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onPasswordTextInputTextChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    //Initializes everything related to Google Sign In
    private void initGoogleLogIn(){
        SignInButton googleBtn = rootView.findViewById(R.id.login_google_button);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onGoogleLogInClick();
            }
        });
    }

    /**
     * Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.onGoogleSignInResult(result);
        }
    }

    private void initBottomButtons(){
        TextView newAccount = rootView.findViewById(R.id.login_newaccount);
        final ViewPager viewPager = getActivity().findViewById(R.id.login_viewpager);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        TextView forgotPassword = rootView.findViewById(R.id.login_forgotpassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
    }
}
