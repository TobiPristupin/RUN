package com.tobipristupin.simplerun.ui.editor.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.tobipristupin.simplerun.R;

import java.text.DecimalFormat;

/**
 * Created by Tobias on 24/01/2017.
 */

public class TimeDialog  {

    private View rootView;
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;
    private NumberPicker numberPickerSecond;
    private onPositiveButtonListener listener;

    public TimeDialog(onPositiveButtonListener listener) {
        this.listener = listener;
    }

    @SuppressLint("InflateParams")
    public AlertDialog makeDialog(Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.time_dialog, null);
        builder.setView(rootView);

        initNumberPickers();

        builder.setTitle("Time");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(formatValues());
            }
        });

        return builder.create();

    }

    private void initNumberPickers(){
        numberPickerHour = rootView.findViewById(R.id.time_picker_hour);
        numberPickerHour.setMaxValue(24);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setValue(0);

        numberPickerMinute = rootView.findViewById(R.id.time_picker_minute);
        numberPickerMinute.setMaxValue(59);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setValue(0);

        numberPickerSecond = rootView.findViewById(R.id.time_picker_second);
        numberPickerSecond.setMaxValue(59);
        numberPickerSecond.setMinValue(0);
        numberPickerSecond.setValue(0);
    }

    /**
     * Gets data from views and formats them for display.
     * @return formatted value.
     */
    private String formatValues(){
        DecimalFormat df = new DecimalFormat("00");
        String time = df.format(numberPickerHour.getValue()) + ":"
                + df.format(numberPickerMinute.getValue()) + ":"
                + df.format(numberPickerSecond.getValue());
        return time;
    }

    public interface onPositiveButtonListener {
        void onClick(String timeValue);
    }
}
