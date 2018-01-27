package com.example.tobias.run.editor.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.SharedPreferenceRepository;

/**
 * Dialog that allows user to set a distance value for a new tracked run. Sends data to parent Activity
 * via onPositiveButtonListener.
 */

public class DistanceDialog {


    private View rootView;
    private NumberPicker numberPickerWhole;
    private NumberPicker numberPickerDecimal;
    private onPositiveButtonListener listener;
    private SharedPreferenceRepository preferenceManager;

    public DistanceDialog(SharedPreferenceRepository sharedPref, onPositiveButtonListener listener) {
        this.listener = listener;
        this.preferenceManager = sharedPref;
    }

    @SuppressLint("InflateParams")
    public AlertDialog makeDialog(Activity activity){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.distance_dialog, null);
        builder.setView(rootView);

        initNumberPickers();
        initUnitText();

        builder.setTitle("Distance");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
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
        unitText.setText(preferenceManager.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY).toString());
    }

    /**
     * Gets values from number pickers and formats them for display.
     * @return formatted value.
     */
    private String formatValues(){
        String distance = null;
        distance = "" + numberPickerWhole.getValue() + "." + numberPickerDecimal.getValue() + " " +
                preferenceManager.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
        return distance;
    }

    public interface onPositiveButtonListener {
        void onClick(String distanceValue);
    }

}


