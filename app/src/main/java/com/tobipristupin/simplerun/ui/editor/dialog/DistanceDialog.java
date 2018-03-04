package com.tobipristupin.simplerun.ui.editor.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.tobipristupin.simplerun.R;
import com.tobipristupin.simplerun.data.interfaces.PreferencesRepository;

/**
 * Dialog that allows user to set a distance value for a new tracked run. Sends data to parent Activity
 * via onPositiveButtonListener.
 */

public class DistanceDialog {


    private View rootView;
    private NumberPicker numberPickerWhole;
    private NumberPicker numberPickerDecimal;
    private onPositiveButtonListener listener;
    private PreferencesRepository preferencesRepository;

    public DistanceDialog(onPositiveButtonListener listener, PreferencesRepository repository) {
        this.listener = listener;
        this.preferencesRepository = repository;
    }

    @SuppressLint("InflateParams")
    public AlertDialog makeDialog(Activity activity){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.distance_dialog, null);
        builder.setView(rootView);

        initNumberPickers();
        initUnitText();

        builder.setTitle(R.string.distance_dialog_distance);


        builder.setNegativeButton(R.string.distance_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton(R.string.distance_dialog_done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(formatValues());
                dialog.dismiss();
            }
        });

        return builder.create();

    }

    private void initNumberPickers(){
        numberPickerWhole = rootView.findViewById(R.id.distance_picker_whole);
        numberPickerWhole.setMaxValue(99);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);

        numberPickerDecimal = rootView.findViewById(R.id.distance_picker_decimal);
        numberPickerDecimal.setMaxValue(9);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);
    }

    /**
     * Checks if distance unit is set to km or mi, and sets text of TextView accordingly.
     */
    private void initUnitText(){
        TextView unitText = rootView.findViewById(R.id.distance_unit);
        unitText.setText(preferencesRepository.getDistanceUnit().toString());
    }

    /**
     * Gets values from number pickers and formats them for display.
     * @return formatted value.
     */
    private String formatValues(){
        String distance;
        distance = "" + numberPickerWhole.getValue() + "." + numberPickerDecimal.getValue() + " " +
                preferencesRepository.getDistanceUnit().toString();
        return distance;
    }

    public interface onPositiveButtonListener {
        void onClick(String distanceValue);
    }

}


