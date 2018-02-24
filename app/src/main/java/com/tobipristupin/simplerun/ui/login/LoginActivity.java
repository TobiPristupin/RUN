package com.tobipristupin.simplerun.ui.login;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.auth.GoogleAuthUtils;
import com.tobipristupin.simplerun.ui.login.adapter.LoginFragmentPagerAdapter;
import com.tobipristupin.simplerun.utils.LoginPageTransformer;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";
    private GoogleSignInOptions signInOptions;
    private GoogleApiClient apiClient;
    private Toast connectionFailedToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInOptions = GoogleAuthUtils.getSignInOptions(LoginActivity.this);
        apiClient = GoogleAuthUtils.getApiClient(LoginActivity.this, signInOptions, new GoogleAuthUtils.ApiClientCallback() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.w(TAG, "Google connection failed " + connectionResult.getErrorMessage());
                showOnConnectionFailedToast();
            }
        });

        ViewPager viewPager = findViewById(R.id.login_viewpager);
        viewPager.setPageTransformer(false, new LoginPageTransformer());
        viewPager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private void showOnConnectionFailedToast(){
        if (connectionFailedToast != null){
            connectionFailedToast.cancel();
        }

        String str = getString(R.string.login_activity_unabletoconnect_toast);
        connectionFailedToast = Toasty.warning(LoginActivity.this, str);

        connectionFailedToast.show();
    }

    public GoogleApiClient getGoogleApiClient(){
        return apiClient;
    }


}