package com.tobipristupin.simplerun.ui.main;




import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseAppCompatActivity;
import com.tobipristupin.simplerun.ui.sharedui.AboutDialog;
import com.tobipristupin.simplerun.ui.history.HistoryFragmentView;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.tobipristupin.simplerun.ui.settings.SettingsActivityView;
import com.tobipristupin.simplerun.ui.settings.libraries.LibraryItemsActivityView;
import com.tobipristupin.simplerun.ui.stats.StatsFragmentView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Main Activity and entry point of application. Implements MainView interface.
 *
 *@author Tobias Pristupin
 *@version 1.0
 *@since January 9 2017
 */
public class MainActivityView extends BaseAppCompatActivity implements MainView {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MainPresenter presenter;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        drawerLayout = findViewById(R.id.main_activity_drawer_layout);
        navigationView = findViewById(R.id.main_navigation_view);
        this.savedInstanceState = savedInstanceState;

        presenter.onCreateView();
    }

    private void initDrawerLayout(){
        navigationView.setNavigationItemSelectedListener(item -> {
            openFragment(item);
            return true;
        });
    }

    private void initNavHeader(){
        NavigationView navigationView = findViewById(R.id.main_navigation_view);
        View headerLayout = navigationView.getHeaderView(0);
        ((TextView) headerLayout.findViewById(R.id.navheader_username_text)).setText(presenter.getUserDisplayName());
        ((TextView) headerLayout.findViewById(R.id.navheader_email_text)).setText(presenter.getUserEmail());

        CircleImageView profileImage = headerLayout.findViewById(R.id.navheader_profile_image);


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

    @Override
    public void initViews() {
        initDrawerLayout();
        initNavHeader();
        setSupportActionBar(R.id.main_toolbar, R.drawable.ic_navigation_menu);

        if (savedInstanceState == null) {
            //If app hasn't loaded the views previously
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.navview_menu_history);
            //Open History fragment setting it as default for startup.
            openFragment(menuItem);
        }
    }

    @Override
    public void showAboutDialog() {
        AboutDialog.showDialog(MainActivityView.this);
    }

    @Override
    public void sendPlayStoreRatingIntent() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        //Flags required to maintain backstack when leaving to play store
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Crashlytics.logException(e);

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void openFragment(MenuItem menuItem){
        Fragment newFragment = null;

        switch (menuItem.getItemId()){
            case R.id.navview_menu_history:
                newFragment = new HistoryFragmentView();
                break;
            case R.id.navview_menu_stats:
                newFragment = new StatsFragmentView();
                break;
            case R.id.navview_menu_settings:
                startActivity(new Intent(MainActivityView.this, SettingsActivityView.class));
                break;
            case R.id.navview_menu_about:
                presenter.onAboutClicked();
                break;
            case R.id.navview_menu_libs:
                Intent intent = new Intent(MainActivityView.this, LibraryItemsActivityView.class);
                intent.putExtra(LibraryItemsActivityView.callingActivityKey, MainActivityView.class.toString());
                startActivity(intent);
                break;
            case R.id.navview_menu_rate_us:
                presenter.onRateUsClicked();
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
}

