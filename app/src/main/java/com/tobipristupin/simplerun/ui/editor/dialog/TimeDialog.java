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

public class TimeDialog {

    public interface onPositiveButtonListener {
        void onClick(int hour, int minute, int second);
    }

    private View rootView;
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;
    private NumberPicker numberPickerSecond;
    private onPositiveButtonListener listener;
    private AlertDialog.Builder builder;

    public TimeDialog(onPositiveButtonListener listener) {
        this.listener = listener;
    }

    @SuppressLint("InflateParams")
    public AlertDialog makeDialog(Activity activity) {
        builder = new AlertDialog.Builder(activity);

        rootView = setViewAndReturnIt(activity);

        initNumberPickers();

        setTitle();
        registerOnNegativeClick();
        registerOnPositiveClick();

        return builder.create();
    }

    private void initNumberPickers() {
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

    @SuppressLint("InflateParams")
    private View inflateLayout(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        return inflater.inflate(R.layout.time_dialog, null);
    }

    private View setViewAndReturnIt(Activity activity){
        View view = inflateLayout(activity);
        builder.setView(view);
        return view;
    }

    private void setTitle(){
        builder.setTitle(R.string.time_dialog_title);
    }

    private void registerOnNegativeClick(){
        builder.setNegativeButton(R.string.time_dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    private void registerOnPositiveClick(){
        builder.setPositiveButton(R.string.time_dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(numberPickerHour.getValue(), numberPickerMinute.getValue(),
                        numberPickerSecond.getValue());
            }
        });
    }

//    private String formatValues() {
//        DecimalFormat df = new DecimalFormat("00");
//        String time = df.format(numberPickerHour.getValue()) + ":"
//                + df.format(numberPickerMinute.getValue()) + ":"
//                + df.format(numberPickerSecond.getValue());
//        return time;
//    }
}
