package com.example.tobias.run;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

/**
 * Custom dialog that allows user to add rating value in editor activity.
 */
public class RatingPickerDialog {

    private Activity activity;
    private View rootView;

    public RatingPickerDialog(Activity activity){
        this.activity = activity;
    }

    public AlertDialog make(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.rating_dialog, null);
        builder.setView(rootView);

        builder.setTitle("Rating");

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

        initRatingBar();

        return builder.create();
    }

    /**
     * Init rating bar and add callback
     */
    private void initRatingBar(){
        final RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rating_rating_bar);
        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
