package com.example.tobias.run.settings.libraries;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.example.tobias.run.R;
import com.example.tobias.run.app.MainActivityView;
import com.example.tobias.run.data.Library;
import com.example.tobias.run.settings.SettingsActivityView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class that shows all open source libraries used in project with a recycler view.
 * Accessed via Settings -> Open Source libraries.
 * <p>
 * When sending intents to this activity, class.toString of activity should be passed as an extra using
 * callingActivityKey.
 */

public class LibraryActivityView extends AppCompatActivity {

    public static final String callingActivityKey = "callFrom";
    private List<Library> libraries = new ArrayList<>();
    private String callingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_view);

        try {
            callingActivity = getIntent().getExtras().getString(callingActivityKey);
        } catch (Exception e) {
            throw new RuntimeException("Missing calling activity data in intent");
        }

        initToolbar();
        populateLibrariesList();
        initRecyclerView();
    }

    /**
     * Returns parent activity to return once "up" button is pressed. Overrided because this activity may
     * be instantiated either directly from {@link com.example.tobias.run.settings.SettingsActivityView} or from
     * {@link com.example.tobias.run.app.MainActivityView} drawer layout, so the previous activity has to be determined
     * at runtime in order to return navigation. Both {@link LibraryActivityView#getSupportParentActivityIntent()} and
     * {@link LibraryActivityView#getParentActivityIntent()} have to be overrided to ensure consistency across android apis.
     *
     * @return Parent activity to return once "up" button is pressed.
     */
    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    /**
     * Returns parent activity to return once "up" button is pressed. Overrided because this activity may
     * be instantiated either directly from {@link com.example.tobias.run.settings.SettingsActivityView} or from
     * {@link com.example.tobias.run.app.MainActivityView} drawer layout, so the previous activity has to be determined
     * at runtime in order to return navigation. Both {@link LibraryActivityView#getSupportParentActivityIntent()} and
     * {@link LibraryActivityView#getParentActivityIntent()} have to be overrided to ensure consistency across android apis.
     *
     * @return Parent activity to return once "up" button is pressed.
     */
    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    private Intent getParentActivityIntentImpl() {
        Intent i;

        if (callingActivity.equals(SettingsActivityView.class.toString())) {
            i = new Intent(LibraryActivityView.this, SettingsActivityView.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if (callingActivity.equals(MainActivityView.class.toString())) {
            i = new Intent(LibraryActivityView.this, MainActivityView.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            throw new RuntimeException("Calling activity could not be determined");
        }

        return i;
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.settings_libraries_toolbar);
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

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.settings_libraries_recyclerview);
        LibrariesAdapter.onClickListener listener = new LibrariesAdapter.onClickListener() {
            @Override
            public void onWebsiteButtonClick(Library library) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(library.getUrl()));
                startActivity(intent);
            }
        };

        LibrariesAdapter adapter = new LibrariesAdapter(libraries, listener);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(LibraryActivityView.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(LibraryActivityView.this, LinearLayoutManager.VERTICAL));
    }

    private void populateLibrariesList() {
        libraries.add(new Library("MPAndroid Chart", "https://developer.android.com/guide/components/intents-common.html"));
    }


}
