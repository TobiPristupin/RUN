package com.example.tobias.run;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for custom ListView in History Fragment.
 */
public class HistoryListItemAdapter extends ArrayAdapter<TrackedRun> {

    private Context context;

    public HistoryListItemAdapter(Context context, ArrayList<TrackedRun> items){
        super(context, 0, items);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.history_list_item,
                    parent,
                    false);
        }


        TrackedRun currentItem = getItem(position);
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_key), Context.MODE_PRIVATE);


        //Set corresponding data on views.
        TextView distance = (TextView) convertView.findViewById(R.id.distance_text);
        DecimalFormat df = new DecimalFormat("0.00");
        String distanceText = String.valueOf(df.format(currentItem.getDistance() / 100)) + " " +
                sharedPref.getString("distance_unit", null); //
        // Shared Pref distance_unit is initialized by default to km and can be changed to mi in settings
        distance.setText(distanceText);

        TextView time = (TextView) convertView.findViewById(R.id.time_text);
        long currentItemTime = currentItem.getTime();
        Period period = new Period(currentItemTime);
        String timeText = String.format(Locale.US, "%02d", period.getHours()) + ":"
                + String.format(Locale.US,"%02d", period.getMinutes()) + ":"
                 + String.format(Locale.US,"%02d", period.getSeconds());
        time.setText(timeText);

        TextView date = (TextView) convertView.findViewById(R.id.date_text);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("E, e/MMM/YYYY");
        String dateText = formatter.print(new DateTime(currentItem.getDate() * 1000L));
        date.setText(dateText);


        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
        ratingBar.setRating(currentItem.getRating());



        return convertView;
    }
}
