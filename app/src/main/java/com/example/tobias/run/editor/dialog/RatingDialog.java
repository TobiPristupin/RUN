package com.example.tobias.run.editor.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.example.tobias.run.R;

/**
 * Created by Tobias on 24/01/2017.
 */

public class RatingDialog {

    private View rootView;
    private onPositiveButtonListener listener;
    private RatingBar ratingBar;

    public RatingDialog(onPositiveButtonListener listener) {
        this.listener = listener;
    }

    public AlertDialog makeDialog(Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.rating_dialog, null);
        builder.setView(rootView);

        initRatingBar();

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
                listener.onClick(formatValues());
            }
        });

        return builder.create();
    }

    private void initRatingBar(){
        ratingBar = rootView.findViewById(R.id.rating_rating_bar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating < 1) {
                    ratingBar.setRating(1);
                }
            }
        });
    }

    /**
     * Gets value from rating bar and formats them for display.
     * @return formatted value.
     */
    private String formatValues(){
        String value = "" + (int) ratingBar.getRating();
        return value;
    }

    public interface onPositiveButtonListener {
        void onClick(String ratingValue);
    }


}
