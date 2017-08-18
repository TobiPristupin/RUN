package com.example.tobias.run.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.tobias.run.R;
import com.example.tobias.run.login.NewAccountActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import es.dmoral.toasty.Toasty;

/**
 * Utils class for Google Sign In  Auth process.
 */

public class GoogleAuthManager {

    private Context context;

    public GoogleAuthManager(Context context){
        this.context = context;
    }

    public GoogleSignInOptions getSignInOptions(){
        GoogleSignInOptions googleSignIn = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return googleSignIn;
    }

    public GoogleApiClient getApiClient(FragmentActivity fragmentActivity, GoogleSignInOptions signInOptions, final String tag){
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(tag, "GoogleConnectionFailed: " + connectionResult);
                        Toasty.warning(context, "Unable to connect to Google. Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        return googleApiClient;
    }
}
