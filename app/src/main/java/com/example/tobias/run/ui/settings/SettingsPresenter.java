package com.example.tobias.run.ui.settings;

import android.content.DialogInterface;

import com.example.tobias.run.data.interfaces.SharedPreferenceRepository;
import com.example.tobias.run.data.model.Distance;
import com.example.tobias.run.ui.settings.dialogs.DistanceUnitDialog;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Presenter for SettingsView implementation
 */

public class SettingsPresenter {

    private SettingsView view;
    private SharedPreferenceRepository sharedPref;

    public SettingsPresenter(SettingsView view, SharedPreferenceRepository sharedPref) {
        this.view = view;
        this.sharedPref = sharedPref;
    }

    public void onCreateView() {
        String distanceUnitText = formatDistanceUnitText(getDistanceUnit());
        view.setDistanceUnitText(distanceUnitText);
    }

    public void onDistanceUnitClick() {
        DistanceUnitDialog.OnClickListener listener = new DistanceUnitDialog.OnClickListener() {
            @Override
            public void onOptionSelected(Distance.Unit unit) {
                view.setDistanceUnitText(formatDistanceUnitText(unit));
                sharedPref.setDistanceUnit(unit);
            }
        };

        view.showDistanceUnitDialog(listener);
    }

    public void onSignOutClick() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                view.loadLogIn();
                dialog.dismiss();
            }
        };

        view.showSignOutDialog(listener);
    }

    public void onHelpAndFeedbackClick() {
        view.sendEmailIntent("justrunapp@gmail.com", "Help and Feedback", "If submitting " +
                "a bug report, please add device model and Android version.");
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

    private String formatDistanceUnitText(Distance.Unit unit) {
        String text;
        if (unit == Distance.Unit.KM) {
            text = "Metric (" + Distance.Unit.KM + ")";
        } else {
            text = "Imperial (" + Distance.Unit.MILE + ")";
        }

        return text;
    }

    private Distance.Unit getDistanceUnit() {
        return sharedPref.getDistanceUnit();
    }
}
