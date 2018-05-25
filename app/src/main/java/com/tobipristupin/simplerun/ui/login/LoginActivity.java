package com.tobipristupin.simplerun.ui.login;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseAppCompatActivity;
import com.tobipristupin.simplerun.auth.GoogleAuthUtils;
import com.tobipristupin.simplerun.ui.login.forgotpassword.ForgotPasswordFragmentView;
import com.tobipristupin.simplerun.ui.login.loginview.LoginFragmentView;
import com.tobipristupin.simplerun.ui.login.newaccount.NewAccountFragmentView;
import com.tobipristupin.simplerun.ui.sharedui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.adapter.LoginFragmentPagerAdapter;
import com.tobipristupin.simplerun.utils.LogWrapper;
import com.tobipristupin.simplerun.utils.LoginPageTransformer;


public class LoginActivity extends BaseAppCompatActivity implements PageChanger, GoogleApiClientProvider {

    private final String TAG = "LoginActivity";
    private GoogleApiClient apiClient;
    private ToastyWrapper connectionFailedToast = new ToastyWrapper();
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions signInOptions = GoogleAuthUtils.getSignInOptions(LoginActivity.this);
        apiClient = GoogleAuthUtils.getApiClient(LoginActivity.this, signInOptions, connectionResult -> {
            LogWrapper.warn(TAG, "Google connection failed " + connectionResult.getErrorMessage());
            showOnConnectionFailedToast();
        });

        viewPager = findViewById(R.id.login_viewpager);
        viewPager.setPageTransformer(false, new LoginPageTransformer());
        viewPager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager()));
        changeTo(Page.LOGIN);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private void showOnConnectionFailedToast(){
        connectionFailedToast.showWarning(LoginActivity.this, getString(R.string.login_activity_unabletoconnect_toast));
    }

    @Override
    public GoogleApiClient getClient() {
        return apiClient;
    }

    @Override
    public void changeTo(Page page) {
        viewPager.setCurrentItem(positionFromPage(page));
    }

    private int positionFromPage(Page page){
        switch (page) {
            case FORGOT_PASSWORD:
                return 0;
            case LOGIN:
                return 1;
            case NEW_ACCOUNT:
                return 2;
        }

        throw new RuntimeException("No position for page");
    }

    private class LoginFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public LoginFragmentPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            return Page.values().length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    return new ForgotPasswordFragmentView();
                case 1 :
                    return new LoginFragmentView();
                case 2 :
                    return new NewAccountFragmentView();
            }

            throw new RuntimeException();
        }
    }
}
