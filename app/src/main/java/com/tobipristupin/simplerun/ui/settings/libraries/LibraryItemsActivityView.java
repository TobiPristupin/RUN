package com.tobipristupin.simplerun.ui.settings.libraries;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseAppCompatActivity;
import com.tobipristupin.simplerun.data.model.LibraryItem;
import com.tobipristupin.simplerun.ui.main.MainActivityView;
import com.tobipristupin.simplerun.ui.settings.SettingsActivityView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class that shows all open source libraries used in project with a recycler view.
 * Accessed via Settings -> Open Source libraries.
 * <p>
 * When sending intents to this activity, class.toString of activity should be passed as an extra using
 * callingActivityKey.
 */

public class LibraryItemsActivityView extends BaseAppCompatActivity {

    public static final String callingActivityKey = "callFrom";
    private List<LibraryItem> libraries = new ArrayList<>();
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

        setSupportActionBar(R.id.settings_libraries_toolbar, R.drawable.ic_arrow_back_white_24dp);
        changeStatusBarColor(R.color.colorPrimaryDark);
        populateLibrariesList();
        initRecyclerView();
    }

    /**
     * Returns parent activity to return once "up" button is pressed. Overrided because this activity may
     * be instantiated either directly from {@link com.tobipristupin.simplerun.ui.settings.SettingsActivityView} or from
     * {@link MainActivityView} drawer layout, so the previous activity has to be determined
     * at runtime in order to return navigation. Both {@link LibraryItemsActivityView#getSupportParentActivityIntent()} and
     * {@link LibraryItemsActivityView#getParentActivityIntent()} have to be overrided to ensure consistency across android apis.
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
     * be instantiated either directly from {@link com.tobipristupin.simplerun.ui.settings.SettingsActivityView} or from
     * {@link MainActivityView} drawer layout, so the previous activity has to be determined
     * at runtime in order to return navigation. Both {@link LibraryItemsActivityView#getSupportParentActivityIntent()} and
     * {@link LibraryItemsActivityView#getParentActivityIntent()} have to be overrided to ensure consistency across android apis.
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
            i = new Intent(LibraryItemsActivityView.this, SettingsActivityView.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if (callingActivity.equals(MainActivityView.class.toString())) {
            i = new Intent(LibraryItemsActivityView.this, MainActivityView.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            throw new RuntimeException("Calling activity could not be determined");
        }

        return i;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.settings_libraries_recyclerview);
        LibrariesItemAdapter.onClickListener listener = new LibrariesItemAdapter.onClickListener() {
            @Override
            public void onWebsiteButtonClick(LibraryItem library) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(library.getUrl()));
                startActivity(intent);
            }
        };

        LibrariesItemAdapter adapter = new LibrariesItemAdapter(libraries, listener);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(LibraryItemsActivityView.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(LibraryItemsActivityView.this, LinearLayoutManager.VERTICAL));
    }

    private void populateLibrariesList() {
        libraries.add(new LibraryItem("Joda Time", "http://www.joda.org/joda-time/"));
        libraries.add(new LibraryItem("MPAndroid Chart", "https://github.com/PhilJay/MPAndroidChart"));
        libraries.add(new LibraryItem("Toasty", "https://github.com/GrenderG/Toasty"));
        libraries.add(new LibraryItem("FabButton", "https://github.com/ckurtm/FabButton"));
        libraries.add(new LibraryItem("AVLoadingIndicatorView", "https://github.com/81813780/AVLoadingIndicatorView"));
        libraries.add(new LibraryItem("Recycler View Animator", "https://github.com/wasabeef/recyclerview-animators"));
        libraries.add(new LibraryItem("Material Spinner", "https://github.com/jaredrummler/MaterialSpinner"));
        libraries.add(new LibraryItem("Circle Image View", "https://github.com/hdodenhof/CircleImageView"));
        libraries.add(new LibraryItem("shared-firebase-preferences", "https://github.com/crysxd/shared-firebase-preferences"));
    }


}
