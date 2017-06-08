package com.example.tobias.run.activities;

/**
 * RUN. application let's users track their running by adding them manually
 * with a high level of customization and visualize them in varied ways,
 * such as in various graphs and ordered by several criteria.
 *
 *@author Tobias Pristupin
 *@version 1.0
 *@since 09/01/2017
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.tobias.run.historyscreen.HistoryFragment;
import com.example.tobias.run.R;
import com.example.tobias.run.loginscreen.LoginActivity;
import com.example.tobias.run.statsscreen.StatsFragment;
import com.google.firebase.auth.*;


/**
 * MainActivity and entry-point of application.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogIn();
        }

        initSharedPref();
        initToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (savedInstanceState == null) {
            //If app hasn't loaded the views previously
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.menu_history);
            //Open History fragment setting it as default for startup.
            openFragment(menuItem);
        }



        //On item selected in navigation view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 openFragment(item);
                 return true;
             }
         });
    }

    private void loadLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        //Flags prevent user from returning to MainActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void openFragment(MenuItem menuItem){
        boolean fragmentTransaction = false;
        Fragment newFragment = null;

        switch (menuItem.getItemId()){
            //If item selected id is menu_history, create new HistoryFragment in newFragment variable
            case R.id.menu_history :
                fragmentTransaction = true;
                newFragment = new HistoryFragment();
                break;
            //...
            case R.id.menu_stats :
                fragmentTransaction = true;
                newFragment = new StatsFragment();
                break;
            //...
            case R.id.menu_settings :
                Log.i("Navigation View", "Settings");
                break;
        }

        //If fragmentTransaction was set to true (button was clicked in NavigationDrawer)
        if (fragmentTransaction){
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
        //If distance_unit hasn't been initialized, set it to default value (km)
        if (sharedPref.getString("distance_unit", null) == null){
            sharedPref.edit().putString("distance_unit", "km").apply();
        }
    }

    /**
     * Set toolbar defined in xml layout as supprot action toolbar and add button to open DrawerLayout
     */
    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
    }

}

