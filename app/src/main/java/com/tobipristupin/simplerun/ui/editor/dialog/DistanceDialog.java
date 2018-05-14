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
import com.tobipristupin.simplerun.data.repository.PreferencesRepository;

/**
 * Dialog that allows user to set a distance value for a new tracked run. Sends data to parent Activity
 * via onPositiveButtonListener.
 */

public class DistanceDialog {

    public interface onPositiveButtonListener {
        void onClick(int wholeNum, int fractionalNum);
    }

    private View rootView;
    private NumberPicker numberPickerWhole;
    private NumberPicker numberPickerDecimal;
    private onPositiveButtonListener listener;
    private PreferencesRepository preferencesRepository;
    private AlertDialog.Builder builder;

    public DistanceDialog(PreferencesRepository repository, onPositiveButtonListener listener) {
        this.listener = listener;
        this.preferencesRepository = repository;
    }

    public AlertDialog makeDialog(Activity activity) {
        builder = new AlertDialog.Builder(activity);

        rootView = setViewAndReturnIt(activity);

        initNumberPickers();
        initUnitText();

        setTitle();
        registerOnNegativeButton();
        registerOnPositiveButton();

        return builder.create();
    }

    private void initNumberPickers() {
        numberPickerWhole = rootView.findViewById(R.id.distance_picker_whole);
        numberPickerWhole.setMaxValue(99);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);

        numberPickerDecimal = rootView.findViewById(R.id.distance_picker_decimal);
        numberPickerDecimal.setMaxValue(9);
        numberPickerWhole.setMinValue(0);
        numberPickerWhole.setValue(1);
    }

    private void initUnitText() {
        TextView unitText = rootView.findViewById(R.id.distance_unit);
        unitText.setText(preferencesRepository.getDistanceUnit().toString());
    }

    @SuppressLint("InflateParams")
    private View inflateLayout(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        return inflater.inflate(R.layout.distance_dialog, null);
    }

    private void onNegativeClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    private void onPositiveClick(DialogInterface dialog, int which){
        listener.onClick(numberPickerWhole.getValue(), numberPickerDecimal.getValue());
        dialog.dismiss();
    }

    private View setViewAndReturnIt(Activity activity){
        View view = inflateLayout(activity);
        builder.setView(view);
        return view;
    }

    private void setTitle(){
        builder.setTitle(R.string.distance_dialog_distance);
    }

    private void registerOnNegativeButton(){
        builder.setNegativeButton(R.string.distance_dialog_cancel, (dialog, which) -> onNegativeClick(dialog, which));
    }

    private void registerOnPositiveButton(){
        builder.setPositiveButton(R.string.distance_dialog_done, (dialog, which) -> onPositiveClick(dialog, which));
    }

}


