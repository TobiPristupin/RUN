package com.example.tobias.run.editorscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.tobias.run.R;

/**
 * Dialog that allows user to set a distance value for a new tracked run. Sends data to parwnt Activity
 * via onPositiveButtonListener.
 */

public class DistanceDialog {


    public interface onPositiveButtonListener {
        public void onClick(String distanceValue);
    }

    private View rootView;
    private NumberPicker numberPickerWhole;
    private NumberPicker numberPickerDecimal;
    private SharedPreferences sharedPref;
    private onPositiveButtonListener listener;
    private Activity activity;

    public DistanceDialog(Activity activity, onPositiveButtonListener listener){
        this.activity = activity;
        this.listener = listener;
    }

    public AlertDialog makeDialog(Activity activity){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.distance_dialog, null);
        builder.setView(rootView);

        sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.preference_key), Context.MODE_PRIVATE);

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
        numberPickerWhole = (NumberPicker)
                rootView.findViewById(R.id.distance_picker_whole);
        numberPickerWhole.setMaxValue(99);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);

        numberPickerDecimal = (NumberPicker)
                rootView.findViewById(R.id.distance_picker_decimal);
        numberPickerDecimal.setMaxValue(99);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);
    }

    /**
     * Checks if distance unit is set to km or mi, and sets text of TextView accordingly.
     */
    private void initUnitText(){
        TextView unitText = (TextView) rootView.findViewById(R.id.distance_unit);
        if(sharedPref.getString("distance_unit", null).equals("km")){
            unitText.setText("km");
        } else {
            unitText.setText("mi");
        }
    }

    /**
     * Gets values from number pickers and formats them for display.
     * @return formatted value.
     */
    private String formatValues(){
        String distance = null;
        if (sharedPref.getString("distance_unit", null).equals("km")){
            distance = "" + numberPickerWhole.getValue() + ","
                    + numberPickerDecimal.getValue() + " km";
        } else {
            distance = "" + numberPickerWhole.getValue() + ","
                    + numberPickerDecimal.getValue() + " mi";
        }
        return distance;
    }

}


