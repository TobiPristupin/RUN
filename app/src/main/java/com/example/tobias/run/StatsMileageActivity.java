package com.example.tobias.run;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class StatsMileageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mileage);
        initToolbar();
        ViewPager viewPager = (ViewPager) findViewById(R.id.mileage_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.mileage_tablayout);

        viewPager.setAdapter(new StatsPagerAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
        }
        return true;
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.stats_mileage_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        changeStatusBarColor();
    }

    /**
     * AppTheme status bar color attr is set to transparent for the drawerLayout in main activity.
     * this activity uses the primary dark color as status bar color. This method sets it during runtime.
     */
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

}
