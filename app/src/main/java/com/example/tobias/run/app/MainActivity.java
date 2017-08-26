package com.example.tobias.run.app;

/**
 * RUN. application let's users track their running by adding them manually
 * with a high level of customization and visualize them in varied ways,
 * such as in various graphs and ordered by several criteria.
 *
 *@author Tobias Pristupin
 *@version 1.0
 *@since 09/01/2017
 */

//TODO: Add downwards and upwards animations when switching activities.
//TODO: Toasty library has bug where text and icon have no padding. Check github to see if issue has been resolved or implement workaround.


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tobias.run.R;
import com.example.tobias.run.database.FirebaseDatabaseManager;
import com.example.tobias.run.history.HistoryFragment;
import com.example.tobias.run.login.LoginActivity;
import com.example.tobias.run.settings.SettingsActivity;
import com.example.tobias.run.stats.StatsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;


/**
 * MainActivity and entry-point of application.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        firebaseAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null){
                    loadLogIn();
                }
            }
        };
        firebaseUser = firebaseAuth.getCurrentUser();

        initDrawerLayout();
        initSharedPref();
        initToolbar();

        if (savedInstanceState == null) {
            //If app hasn't loaded the views previously
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.menu_history);
            //Open History fragment setting it as default for startup.
            openFragment(menuItem);
        }
    }

    private void initDrawerLayout(){
        //initNavHeader();

        //On item selected in navigation view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                openFragment(item);
                return true;
            }
        });
    }

//    private void initNavHeader(){
//        View navHeader = navigationView.getHeaderView(0);
//        ImageView userImage = (ImageView) navHeader.findViewById(R.id.navheader_user_image);
//        RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), firebaseUser.getPhotoUrl().toString());
//        bitmapDrawable.setCircular(true);
//        userImage.setImageDrawable(bitmapDrawable);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authListener);
    }

    private void loadLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        //Flags prevent user from returning to MainActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void openFragment(MenuItem menuItem){
        Fragment newFragment = null;

        switch (menuItem.getItemId()){
            //If item selected id is menu_history, create new HistoryFragment in newFragment variable
            case R.id.menu_history :
                newFragment = new HistoryFragment();
                break;
            //...
            case R.id.menu_stats :
                newFragment = new StatsFragment();
                break;
            //...
            case R.id.menu_settings :
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }

        if (newFragment != null){
            //Replace content frame in activity_main.xml with newFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, newFragment)
                    .commit();

            menuItem.setChecked(true);
            getSupportActionBar().setTitle(menuItem.getTitle());
        }

        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Called when home button is clicked.
        switch (item.getItemId()){
            case android.R.id.home :
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializes shared preferences to the default values if none has been set before.
     */
    public void initSharedPref(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        //If distance_unit hasn't been initialized, set it to default value (mi)
        if (sharedPref.getString(getString(R.string.preference_distance_unit_key), null) == null){
            sharedPref.edit().putString(getString(R.string.preference_distance_unit_key), "mi").apply();
      }
    }


    /**
     * Set toolbar defined in xml layout as support action toolbar and add button to open DrawerLayout
     */
    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
    }

}

