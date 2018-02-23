package com.tobipristupin.simplerun.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tobipristupin.simplerun.BuildConfig;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.data.interfaces.RunRepository;
import com.tobipristupin.simplerun.data.manager.FirebaseRepository;
import com.tobipristupin.simplerun.data.manager.SharedPrefRepository;
import com.tobipristupin.simplerun.data.model.Distance;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.tobipristupin.simplerun.ui.settings.dialogs.DistanceUnitDialog;
import com.tobipristupin.simplerun.ui.settings.libraries.LibraryItemsActivityView;

import es.dmoral.toasty.Toasty;

public class SettingsActivityView extends AppCompatActivity implements SettingsView {

    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        presenter = new SettingsPresenter(this, new SharedPrefRepository(SettingsActivityView.this));

        initToolbar();
        initDistanceUnit();
        initSignOut();
        initLibraries();
        initHelpAndFeedback();
        initAbout();

        presenter.onCreateView();
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

    private void initDistanceUnit() {
        RelativeLayout distanceView = findViewById(R.id.settings_distanceunit_container);
        distanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDistanceUnitClick();
            }
        });
    }

    private void initSignOut() {
        RelativeLayout signOutView = findViewById(R.id.settings_signout_container);
        signOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSignOutClick();
            }
        });
    }

    private void initHelpAndFeedback() {
        RelativeLayout view = findViewById(R.id.settings_help_container);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHelpAndFeedbackClick();
            }
        });
    }

    private void initLibraries() {
        RelativeLayout view = findViewById(R.id.settings_libs_container);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLibrariesClick();
            }
        });
    }

    private void initAbout() {
        RelativeLayout view = findViewById(R.id.settings_about_container);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAboutClick();
            }
        });
    }

    @Override
    public void setDistanceUnitText(Distance.Unit unit) {
        String text;
        if (unit == Distance.Unit.KM) {
            text = getString(R.string.all_metric) + " (" + Distance.Unit.KM + ")";
        } else {
            text = getString(R.string.all_imperial) + " (" + Distance.Unit.MILE + ")";
        }

        TextView distanceUnit = findViewById(R.id.settings_distanceunit_selection);
        distanceUnit.setText(text);
    }

    @Override
    public void showDistanceUnitDialog(DistanceUnitDialog.OnClickListener OnClickListener) {
        new DistanceUnitDialog(OnClickListener, new SharedPrefRepository(SettingsActivityView.this))
                .makeDialog(SettingsActivityView.this).show();
    }

    @Override
    public void showSignOutDialog(final DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivityView.this);
        builder.setMessage(R.string.settings_activity_view_signoutdialog_title);
        builder.setPositiveButton(R.string.settings_activity_view_signoutdialog_positive, onClickListener);
        builder.setNegativeButton(R.string.settings_activity_view_signoutdialog_negative, null);
        builder.create().show();
    }

    @Override
    public void loadLogIn() {
        Intent intent = new Intent(SettingsActivityView.this, LoginActivity.class);
        //Flags prevent user from returning to MainActivityView when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * @param mailTo  recipient
     */
    @Override
    public void sendEmailIntent(String mailTo) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});

        String subject = getString(R.string.settings_activity_view_email_subject);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        String bodyString = getString(R.string.settings_acttivity_view_email_bodyversion) + BuildConfig.VERSION_NAME;
        bodyString += getString(R.string.settings_acttivity_view_email_body_desc);


        intent.putExtra(Intent.EXTRA_TEXT, bodyString);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            presenter.onActivityNotFoundError();
        }
    }

    @Override
    public void showNoEmailAppError() {
        Toasty.warning(SettingsActivityView.this, getString(R.string.settings_activity_view_noemailerror_toast)).show();
    }

    @Override
    public void sendLibrariesViewIntent() {
        Intent intent = new Intent(SettingsActivityView.this, LibraryItemsActivityView.class);
        intent.putExtra(LibraryItemsActivityView.callingActivityKey, SettingsActivityView.class.toString());
        startActivity(intent);
    }

    @Override
    public void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivityView.this);
        builder.setTitle(R.string.settings_activity_view_aboutdialog_title);
        builder.setMessage(String.format(getResources().getString(R.string.about_dialog), BuildConfig.VERSION_NAME));
        builder.show();
    }

}
