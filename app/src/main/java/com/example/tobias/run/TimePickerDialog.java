package com.example.tobias.run;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Spinner;

/**
 * Custom Dialog that allow user to add time value in editor activity.
 */

public class TimePickerDialog {

    private Activity activity;
    private View rootView;

    public TimePickerDialog(Activity activity){
        this.activity = activity;
    }

    public AlertDialog make(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.time_dialog, null);
        builder.setView(rootView);

        builder.setTitle("Time");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        initPickers();

        return builder.create();
    }

    /**
     * Initializes hour, minute and second pickers and sets callbacks
     */
    private void initPickers(){
        NumberPicker numberPickerHour = (NumberPicker) rootView.findViewById(R.id.time_picker_hour);

        numberPickerHour.setMaxValue(24);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setValue(0);

        NumberPicker numberPickerMinute = (NumberPicker) rootView.findViewById(R.id.time_picker_minute);

        numberPickerMinute.setMaxValue(59);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setValue(0);

        NumberPicker numberPickerSecond = (NumberPicker) rootView.findViewById(R.id.time_picker_second);

        numberPickerSecond.setMaxValue(59);
        numberPickerSecond.setMinValue(0);
        numberPickerSecond.setValue(0);

    }
}
