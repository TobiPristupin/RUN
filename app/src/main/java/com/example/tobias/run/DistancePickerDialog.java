package com.example.tobias.run;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;


/**
 * Custom dialog that allows user to add distance value in editor activity.
 */
public class DistancePickerDialog {

    private Activity activity;
    private View rootView;

    public DistancePickerDialog(Activity activity){

        this.activity = activity;
    }

    public AlertDialog make(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.distance_dialog, null);
        builder.setView(rootView);
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
                dialog.dismiss();
            }
        });


        initNumberPickers();

        return builder.create();
    }

    /**
     * Initializes number picker whole and decimal and sets callbacks
     */
    private void initNumberPickers(){
        NumberPicker numberPickerWhole = (NumberPicker) rootView.findViewById(R.id.distance_picker_whole);

        numberPickerWhole.setMaxValue(99);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);

        NumberPicker numberPickerDecimal = (NumberPicker) rootView.findViewById(R.id.distance_picker_decimal);
        numberPickerDecimal.setMaxValue(99);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);

    }




}
