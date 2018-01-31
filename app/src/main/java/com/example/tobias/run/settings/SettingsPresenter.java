package com.example.tobias.run.settings;

import android.content.DialogInterface;

import com.example.tobias.run.data.Distance;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.settings.dialogs.DistanceUnitDialog;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Tobi on 1/27/2018.
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
        DistanceUnitDialog.onClickListener listener = new DistanceUnitDialog.onClickListener() {
            @Override
            public void onOptionSelected(Distance.Unit unit) {
                view.setDistanceUnitText(formatDistanceUnitText(unit));
                sharedPref.set(SharedPreferenceRepository.DISTANCE_UNIT_KEY, unit.toString());
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
        return sharedPref.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
    }
}
