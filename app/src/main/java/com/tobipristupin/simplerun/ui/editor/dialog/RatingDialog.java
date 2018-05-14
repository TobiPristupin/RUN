package com.tobipristupin.simplerun.ui.editor.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.tobipristupin.simplerun.R;

public class RatingDialog {

    public interface onPositiveButtonListener {
        void onClick(int ratingValue);
    }

    private View rootView;
    private onPositiveButtonListener listener;
    private RatingBar ratingBar;
    AlertDialog.Builder builder;

    public RatingDialog(onPositiveButtonListener listener) {
        this.listener = listener;
    }

    @SuppressLint("InflateParams")
    public AlertDialog makeDialog(Activity activity) {
        builder = new AlertDialog.Builder(activity);

        rootView = setViewAndReturnIt(activity);

        initRatingBar();

        setTitle();
        registerOnNegativeButton();
        registerOnPositiveButton();

        return builder.create();
    }

    private void initRatingBar() {
        ratingBar = rootView.findViewById(R.id.rating_rating_bar);

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating < 1) {
                ratingBar.setRating(1);
            }
        });
    }

//    private String formatValues() {
//        return "" + (int) ratingBar.getRating();
//    }

    @SuppressLint("InflateParams")
    private View inflateLayout(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        return inflater.inflate(R.layout.rating_dialog, null);
    }

    private void onNegativeClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    private void onPositiveClick(DialogInterface dialog, int which){
        listener.onClick((int) ratingBar.getRating());
    }

    private View setViewAndReturnIt(Activity activity){
        View view = inflateLayout(activity);
        builder.setView(view);
        return view;
    }

    private void setTitle(){
        builder.setTitle(R.string.rating_dialog_title);
    }

    private void registerOnNegativeButton(){
        builder.setNegativeButton(R.string.rating_dialog_negative_button, (dialog, which) -> onNegativeClick(dialog, which));
    }

    private void registerOnPositiveButton(){
        builder.setPositiveButton(R.string.rating_dialog_positive_button, (dialog, which) -> onPositiveClick(dialog, which));
    }
}
