package com.tobipristupin.simplerun.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tobipristupin.simplerun.BuildConfig;
import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.app.BaseAppCompatActivity;
import com.tobipristupin.simplerun.data.repository.SharedPrefRepository;
import com.tobipristupin.simplerun.data.model.DistanceUnit;
import com.tobipristupin.simplerun.ui.AboutDialog;
import com.tobipristupin.simplerun.ui.ToastyWrapper;
import com.tobipristupin.simplerun.ui.login.LoginActivity;
import com.tobipristupin.simplerun.ui.settings.dialogs.DistanceUnitDialog;
import com.tobipristupin.simplerun.ui.settings.libraries.LibraryItemsActivityView;

public class SettingsActivityView extends BaseAppCompatActivity implements SettingsView {

    private SettingsPresenter presenter;

    private ToastyWrapper noEmailErrorToasty = new ToastyWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        presenter = new SettingsPresenter(this, new SharedPrefRepository(SettingsActivityView.this));

        setSupportActionBar(R.id.settings_toolbar, R.drawable.ic_arrow_back_white_24dp);
        changeStatusBarColor(R.color.colorPrimaryDark);

        initViews();

        presenter.onCreateView();
    }

    private void initViews(){
        initDistanceUnit();
        initSignOut();
        initLibraries();
        initHelpAndFeedback();
        initAbout();
        initLicense();
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

    private void initLicense(){
        RelativeLayout view = findViewById(R.id.settings_license_container);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLicenseClick();
            }
        });
    }

    @Override
    public void setDistanceUnitText(DistanceUnit unit) {
        String text;
        if (unit == DistanceUnit.KM) {
            text = getString(R.string.all_metric) + " (" + DistanceUnit.KM + ")";
        } else {
            text = getString(R.string.all_imperial) + " (" + DistanceUnit.MILE + ")";
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

        String bodyString = "\n\n\n\n" + getString(R.string.settings_acttivity_view_email_bodyversion) + " " + BuildConfig.VERSION_NAME;
        bodyString += "\n" + getString(R.string.settings_acttivity_view_email_body_desc);

        intent.putExtra(Intent.EXTRA_TEXT, bodyString);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            presenter.onActivityNotFoundError();
        }
    }

    @Override
    public void showNoEmailAppError() {
        noEmailErrorToasty.showWarning(SettingsActivityView.this, getString(R.string.settings_activity_view_noemailerror_toast));
    }

    @Override
    public void sendLibrariesViewIntent() {
        Intent intent = new Intent(SettingsActivityView.this, LibraryItemsActivityView.class);
        intent.putExtra(LibraryItemsActivityView.callingActivityKey, SettingsActivityView.class.toString());
        startActivity(intent);
    }

    @Override
    public void showAboutDialog() {
        AboutDialog.showDialog(SettingsActivityView.this);
    }

    @Override
    public void showLicenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivityView.this);
        builder.setTitle(getString(R.string.settings_activity_license));
        builder.setMessage(getText(R.string.license_dialog));
        builder.show();
    }
}
