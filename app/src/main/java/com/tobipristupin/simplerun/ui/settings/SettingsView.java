package com.tobipristupin.simplerun.ui.settings;

import android.content.DialogInterface;

import com.tobipristupin.simplerun.data.model.DistanceUnit;
import com.tobipristupin.simplerun.ui.settings.dialogs.DistanceUnitDialog;

public interface SettingsView {

    void setDistanceUnitText(DistanceUnit unit);

    void showDistanceUnitDialog(DistanceUnitDialog.OnClickListener OnClickListener);

    void showSignOutDialog(DialogInterface.OnClickListener onClickListener);

    void loadLogIn();

    void sendEmailIntent(String mailTo);

    void showNoEmailAppError();

    void sendLibrariesViewIntent();

    void showAboutDialog();
}
