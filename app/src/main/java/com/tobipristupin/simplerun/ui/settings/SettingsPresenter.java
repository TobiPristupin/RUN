package com.tobipristupin.simplerun.ui.settings;

import android.content.DialogInterface;

import com.google.firebase.auth.FirebaseAuth;
import com.tobipristupin.simplerun.data.repository.PreferencesRepository;
import com.tobipristupin.simplerun.data.model.DistanceUnit;
import com.tobipristupin.simplerun.ui.settings.dialogs.DistanceUnitDialog;

/**
 * Presenter for SettingsView implementation
 */

public class SettingsPresenter {

    private SettingsView view;
    private PreferencesRepository preferencesRepository;

    public SettingsPresenter(SettingsView view, PreferencesRepository repository) {
        this.view = view;
        this.preferencesRepository = repository;
    }

    public void onCreateView() {
        view.setDistanceUnitText(getDistanceUnit());
    }

    public void onDistanceUnitClick() {
        DistanceUnitDialog.OnClickListener listener = unit -> {
            preferencesRepository.setDistanceUnit(unit);
            view.setDistanceUnitText(unit);
        };

        view.showDistanceUnitDialog(listener);
    }

    public void onSignOutClick() {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            view.loadLogIn();
            dialog.dismiss();
        };

        view.showSignOutDialog(listener);
    }

    public void onHelpAndFeedbackClick() {
        view.sendEmailIntent("justrunapp@gmail.com");
    }

    public void onAboutClick() {
        view.showAboutDialog();
    }

    public void onActivityNotFoundError() {
        view.showNoEmailAppError();
    }

    public void onLibrariesClick() {
        view.sendLibrariesViewIntent();
    }

    public void onLicenseClick(){
        view.showLicenseDialog();
    }

    private DistanceUnit getDistanceUnit() {
        return preferencesRepository.getDistanceUnit();
    }
}
