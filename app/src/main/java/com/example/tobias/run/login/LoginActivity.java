package com.example.tobias.run.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobias.run.R;
import com.example.tobias.run.app.MainActivity;
import com.example.tobias.run.login.adapter.LoginFragmentPagerAdapter;
import com.example.tobias.run.utils.GoogleAuthManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoginActivity extends AppCompatActivity {

    private GoogleSignInOptions signInOptions;
    private GoogleApiClient apiClient;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleAuthManager authManager = new GoogleAuthManager(LoginActivity.this);
        signInOptions = authManager.getSignInOptions();
        apiClient = authManager.getApiClient(LoginActivity.this, signInOptions, TAG);

        ViewPager viewPager = (ViewPager) findViewById(R.id.login_viewpager);
        viewPager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    public GoogleApiClient getGoogleApiClient(){
        return apiClient;
    }


}
