package com.tobipristupin.simplerun.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.tobipristupin.simplerun.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Utils class for Google Sign In Auth process.
 */

public class GoogleAuthUtils {

    public interface ApiClientCallback {
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }

    public static GoogleSignInOptions getSignInOptions(Context context){
        GoogleSignInOptions googleSignIn = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return googleSignIn;
    }

    public static GoogleApiClient getApiClient(final FragmentActivity fragmentActivity, GoogleSignInOptions signInOptions, ApiClientCallback callback){
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(fragmentActivity)
                .enableAutoManage(fragmentActivity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        callback.onConnectionFailed(connectionResult);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        return googleApiClient;
    }
}
