package com.tobipristupin.simplerun.ui.settings.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tobipristupin.simplerun.data.interfaces.PreferencesRepository;
import com.tobipristupin.simplerun.data.model.Distance;

/**
 * Created by Tobi on 1/27/2018.
 */

public class DistanceUnitDialog {

    private OnClickListener listener;
    private PreferencesRepository preferencesRepository;

    public DistanceUnitDialog(OnClickListener listener, PreferencesRepository repository) {
        this.listener = listener;
        this.preferencesRepository = repository;
    }

    public AlertDialog makeDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Distance Unit");

        Distance.Unit distanceUnit = preferencesRepository.getDistanceUnit();

        int checkedItem = distanceUnit.equals(Distance.Unit.KM) ? 0 : 1;
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
