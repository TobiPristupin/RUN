package com.example.tobias.run.app;

/**
 * RUN. application let's users track their running by adding them manually
 * with a high level of customization and visualize them in varied ways,
 * such as in various graphs and ordered by several criteria.
 *
 *@author Tobias Pristupin
 *@version 1.0
 *@since January 9 2017
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.SharedPreferenceManager;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.history.HistoryFragmentView;
import com.example.tobias.run.login.LoginActivity;
import com.example.tobias.run.settings.SettingsActivity;
import com.example.tobias.run.stats.StatsFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivityView extends AppCompatActivity implements MainView {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MainPresenter presenter;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferenceRepository preferenceRepository = new SharedPreferenceManager(MainActivityView.this);
        presenter = new MainPresenter(this, preferenceRepository);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        this.savedInstanceState = savedInstanceState;

        presenter.authenticate();
    }

    @Override
    public void initViews() {
        initDrawerLayout();
        initToolbar();
        initNavHeader();
        presenter.initSharedPreferences();

        if (savedInstanceState == null) {
            //If app hasn't loaded the views previously
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.menu_history);
            //Open History fragment setting it as default for startup.
            openFragment(menuItem);
        }
    }

    private void initDrawerLayout(){
        //On item selected in navigation view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                openFragment(item);
                return true;
            }
        });
    }

    private void initNavHeader(){
        NavigationView navigationView = findViewById(R.id.main_navigation_view);
        View headerLayout = navigationView.getHeaderView(0);
        ((TextView) headerLayout.findViewById(R.id.navheader_username_text)).setText(presenter.getUserDisplayName());
        ((TextView) headerLayout.findViewById(R.id.navheader_email_text)).setText(presenter.getUserEmail());

        CircleImageView profileImage = (CircleImageView) headerLayout.findViewById(R.id.navheader_profile_image);

        Picasso.with(MainActivityView.this)
                .load(presenter.getUserPhotoUrl())
                .placeholder(android.R.color.darker_gray)
                .into(profileImage);
    }


    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPauseView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResumeView();
    }

    @Override
    public void loadLogIn(){
        Intent intent = new Intent(MainActivityView.this, LoginActivity.class);
        //Flags prevent user from returning to MainActivityView when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void openFragment(MenuItem menuItem){
        Fragment newFragment = null;

        switch (menuItem.getItemId()){
            //If item selected id is menu_history, create new HistoryFragmentView in newFragment variable
            case R.id.menu_history :
                newFragment = new HistoryFragmentView();
                break;
            //...
            case R.id.menu_stats :
                newFragment = new StatsFragment();
                break;
            //...
            case R.id.menu_settings :
                startActivity(new Intent(MainActivityView.this, SettingsActivity.class));
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
     * Set toolbar defined in xml layout as support action toolbar and add button to open DrawerLayout
     */
    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
    }

}

