package com.tobipristupin.simplerun.ui.settings;

import android.content.DialogInterface;

import com.tobipristupin.simplerun.ui.settings.dialogs.DistanceUnitDialog;

/**
 * Created by Tobi on 1/27/2018.
 */

public interface SettingsView {

    void setDistanceUnitText(String text);

    void showDistanceUnitDialog(DistanceUnitDialog.OnClickListener OnClickListener);

    void showSignOutDialog(DialogInterface.OnClickListener onClickListener);

    void loadLogIn();

    void sendEmailIntent(String mailTo, String subject, String body);

    void showNoEmailAppError();

    void sendLibrariesViewIntent();

    void showAboutDialog();
}
