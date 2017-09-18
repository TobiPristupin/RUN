package com.example.tobias.run.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.tobias.run.R;
import com.example.tobias.run.login.adapter.LoginFragmentPagerAdapter;
import com.example.tobias.run.utils.LoginPageTransformer;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
        viewPager.setPageTransformer(false, new LoginPageTransformer());
        viewPager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //TODO: Make google buttons fabs with loading anims?

    }

    public GoogleApiClient getGoogleApiClient(){
        return apiClient;
    }


}
