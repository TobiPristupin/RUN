package com.tobipristupin.simplerun.ui.settings.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.data.repository.PreferencesRepository;
import com.tobipristupin.simplerun.data.model.DistanceUnit;

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

        builder.setTitle(R.string.distance_unit_dialog_distanceunit);

        DistanceUnit distanceUnit = preferencesRepository.getDistanceUnit();

        int checkedItem = distanceUnit.equals(DistanceUnit.KM) ? 0 : 1;
        CharSequence[] items = new String[]{context.getString(R.string.all_metric) + " (" + DistanceUnit.KM + ")"
                , context.getString(R.string.all_imperial)  + " (" + DistanceUnit.MILE + ")"};

        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0) {
                listener.onOptionSelected(DistanceUnit.KM);
            } else {
                listener.onOptionSelected(DistanceUnit.MILE);
            }
            dialog.dismiss();
        });


        return builder.create();
    }

    public interface OnClickListener {
        void onOptionSelected(DistanceUnit unit);
    }
}
