package com.example.tobias.run.ui.settings.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.tobias.run.data.interfaces.SharedPreferenceRepository;
import com.example.tobias.run.data.model.Distance;

/**
 * Created by Tobi on 1/27/2018.
 */

public class DistanceUnitDialog {

    private SharedPreferenceRepository preferenceManager;
    private OnClickListener listener;

    public DistanceUnitDialog(SharedPreferenceRepository preferenceManager, OnClickListener listener) {
        this.preferenceManager = preferenceManager;
        this.listener = listener;
    }

    public AlertDialog makeDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Distance Unit");

        int checkedItem = preferenceManager.getDistanceUnit().equals(Distance.Unit.KM) ? 0 : 1;
        CharSequence[] items = new String[]{"Metric (" + Distance.Unit.KM + ")", "Imperial (" + Distance.Unit.MILE + ")"};
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    listener.onOptionSelected(Distance.Unit.KM);
                } else {
                    listener.onOptionSelected(Distance.Unit.MILE);
                }
                dialog.dismiss();
            }
        });


        return builder.create();
    }

    public interface OnClickListener {
        void onOptionSelected(Distance.Unit unit);
    }
}
