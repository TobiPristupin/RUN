package com.example.tobias.run.settings;

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
import com.example.tobias.run.data.Distance;
import com.example.tobias.run.data.SharedPreferenceManager;
import com.example.tobias.run.data.SharedPreferenceRepository;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferenceRepository sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPref = new SharedPreferenceManager(SettingsActivity.this);

        initToolbar();
        initDistanceUnitDialog();
//        initButtons();
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
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

    private void initDistanceUnitDialog() {
        setDistanceUnitText(sharedPref.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY));

        RelativeLayout distanceView = findViewById(R.id.settings_distanceunit_container);

        distanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistanceUnitDialog dialog = new DistanceUnitDialog(sharedPref,
                        new DistanceUnitDialog.onClickListener() {
                            @Override
                            public void onOptionSelected(Distance.Unit unit) {
                                sharedPref.set(SharedPreferenceRepository.DISTANCE_UNIT_KEY, unit.toString());
                                setDistanceUnitText(unit);
                            }
                        });

                dialog.makeDialog(SettingsActivity.this).show();
            }
        });
    }

    private void setDistanceUnitText(Distance.Unit unit) {
        TextView distanceUnit = findViewById(R.id.settings_distanceunit_selection);

        if (unit == Distance.Unit.KM) {
            distanceUnit.setText("Metric (" + Distance.Unit.KM + ")");
        } else {
            distanceUnit.setText("Imperial (" + Distance.Unit.MILE + ")");
        }
    }

//    private void initButtons(){
//        RelativeLayout signOutButton = (RelativeLayout) findViewById(R.id.settings_signout_button);
//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
//                builder.setMessage("Are you sure you want to log out?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        FirebaseAuth.getInstance().signOut();
//                        loadLogIn();
//                        finish();
//                    }
//                });
//                builder.setNegativeButton("No", null);
//                builder.create().show();
//            }
//        });
//
//        RelativeLayout changeMetricButton = findViewById(R.id.settings_distance_unit_button);
//        changeMetricButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferenceRepository sharedPref = new SharedPreferenceManager(SettingsActivity.this);
//                if (sharedPref.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY).equals(Distance.Unit.KM)){
//                    sharedPref.set(SharedPreferenceRepository.DISTANCE_UNIT_KEY, Distance.Unit.MILE.toString());
//                } else {
//                    sharedPref.set(SharedPreferenceRepository.DISTANCE_UNIT_KEY, Distance.Unit.KM.toString());
//                }
//            }
//        });
//    }
//
//    private void loadLogIn(){
//        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
//        //Flags prevent user from returning to MainActivityView when pressing back button
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }

}
