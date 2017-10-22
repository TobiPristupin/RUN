package com.example.tobias.run.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.SharedPreferenceManager;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();
        initUnitText();
        initButtons();
    }



    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        changeStatusBarColor();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void initUnitText(){
        TextView distanceUnit = (TextView) findViewById(R.id.settings_distance_unit_value);
        SharedPreferenceRepository preferenceManager = new SharedPreferenceManager(SettingsActivity.this);
        if (preferenceManager.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY).equals("km")){
            distanceUnit.setText("Metric (km)");
        } else {
            distanceUnit.setText("Imperial (mi)");
        }
    }

    private void initButtons(){
        RelativeLayout signOutButton = (RelativeLayout) findViewById(R.id.settings_signout_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Are you sure you want to log out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        loadLogIn();
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
            }
        });
    }

    private void loadLogIn(){
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        //Flags prevent user from returning to MainActivityView when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
