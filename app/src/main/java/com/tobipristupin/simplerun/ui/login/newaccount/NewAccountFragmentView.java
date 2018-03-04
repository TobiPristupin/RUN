package com.tobipristupin.simplerun.ui.login.newaccount;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.interfaces.ErrorType;
import com.tobipristupin.simplerun.ui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.BaseLoginFragment;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.tobipristupin.simplerun.ui.main.MainActivityView;
import com.wang.avi.AVLoadingIndicatorView;

public class NewAccountFragmentView extends BaseLoginFragment implements NewAccountView {

    private static final int RC_SIGN_IN = 1379;
    private View rootView;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordLayout2;
    private Button createAccountButton;
    private AVLoadingIndicatorView loadingIndicator;
    private NewAccountPresenter presenter;

    private ToastyWrapper createAccountToast = new ToastyWrapper();
    private ToastyWrapper weakPasswordToast = new ToastyWrapper();
    private ToastyWrapper accountCollisionToast = new ToastyWrapper();
    private ToastyWrapper googleSignInToast = new ToastyWrapper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_account, container, false);

        emailLayout = rootView.findViewById(R.id.new_account_email);
        passwordLayout = rootView.findViewById(R.id.new_account_password1);
        passwordLayout2 = rootView.findViewById(R.id.new_account_password2);
        loadingIndicator = rootView.findViewById(R.id.new_account_loading_indicator);
        createAccountButton = rootView.findViewById(R.id.new_account_create_account_button);

        presenter = new NewAccountPresenter(this);

        setLayoutErrorReset(emailLayout, passwordLayout, passwordLayout2);

        setLayoutErrorReset();
        initCreateAccountButton();
        initGoogleLogIn();
        initReturnButton();

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
    public void enablePasswordError(ErrorType.PasswordLogin type) {
        enablePasswordViewError(passwordLayout, type);
    }

    @Override
    public void enablePassword2Error(ErrorType.PasswordLogin type) {
        enablePasswordViewError(passwordLayout2, type);
    }

    private void enablePasswordViewError(TextInputLayout view, ErrorType.PasswordLogin type){
        view.setErrorEnabled(true);

        switch (type) {
            case REQUIRED_FIELD :
                view.setError(getString(R.string.all_required_field));
                break;
            case SHORT_PASSWORD :
                view.setError(getString(R.string.all_short_password));
                break;
            case INVALID_CREDENTIALS :
                view.setError(getString(R.string.all_invalid_credentials));
                break;
            case PASSWORD_DONT_MATCH :
                view.setError(getString(R.string.all_passwords_dont_match));
                break;
            default :
                view.setError(getString(R.string.all_error));
        }
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
    public void startLoadingAnimation() {
        //Fade out animation
        createAccountButton.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
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
        //Fade in
        loadingIndicator.smoothToHide();
        createAccountButton.animate().alpha(1.0f);
    }

    @Override
    public void showCreateAccountErrorToast() {
        String str = getString(R.string.new_account_fragment_view_account_error_toast);
        createAccountToast.showWarning(getContext(), str);
    }

    @Override
    public void showWeakPasswordErrorToast() {
        String str = getString(R.string.new_account_fragment_view_password_toast);
        weakPasswordToast.showWarning(getContext(), str);
    }

    @Override
    public void showUserCollisionToast() {
        String str = getString(R.string.new_account_fragment_view_collision_toast);
        accountCollisionToast.showWarning(getContext(), str);
    }

    @Override
    public void sendGoogleSignInIntent() {
        GoogleApiClient apiClient = ((LoginActivity) getActivity()).getGoogleApiClient();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showGoogleSignInFailedToast() {
        String str = getString(R.string.new_account_fragment_view_auth_failed_toast);
        googleSignInToast.showWarning(getContext(), str);
    }

    private void initCreateAccountButton() {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailLayout.getEditText().getText().toString().trim();
                final String password = passwordLayout.getEditText().getText().toString().trim();
                final String password2 = passwordLayout2.getEditText().getText().toString().trim();

                presenter.onCreateAccountButtonClick(email, password, password2);
            }
        });

    }

    private void initGoogleLogIn(){
        SignInButton signInButton = rootView.findViewById(R.id.new_account_google_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGoogleLogInClick();
            }
        });

    }

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.onGoogleSignInResult(result);
        }
    }

    private void initReturnButton(){
        Button button = rootView.findViewById(R.id.new_account_return);
        final ViewPager viewPager = getActivity().findViewById(R.id.login_viewpager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }
}
