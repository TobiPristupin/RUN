package com.example.tobias.run;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for custom ListView in History Fragment.
 */
public class HistoryListItemAdapter extends ArrayAdapter<HistoryListItem> {

    private Context context;

    public HistoryListItemAdapter(Context context, ArrayList<HistoryListItem> items){
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


        HistoryListItem currentItem = getItem(position);
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_key), Context.MODE_PRIVATE);


        //Set corresponding data on views.
        TextView distance = (TextView) convertView.findViewById(R.id.distance_text);
        DecimalFormat df = new DecimalFormat("0.00");
        String distanceText = String.valueOf(df.format(currentItem.getDistance())) + " " +
                sharedPref.getString("distance_unit", null); //
        // Shared Pref distance_unit is initialized by deafult to km and can be changed to mi in settings
        distance.setText(distanceText);

        TextView time = (TextView) convertView.findViewById(R.id.time_text);
        Period period = currentItem.getTime();
        String timeText = String.format(Locale.US, "%02d", period.getHours()) + ":"
                + String.format(Locale.US,"%02d", period.getMinutes()) + ":"
                 + String.format(Locale.US,"%02d", period.getSeconds());
        time.setText(timeText);

        TextView date = (TextView) convertView.findViewById(R.id.date_text);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, e/MMM/YYYY");
        String dateText = formatter.print(currentItem.getDate());
        date.setText(dateText);

        ImageView favorite = (ImageView) convertView.findViewById(R.id.favorite_button);
        if (currentItem.isFavorite()){
            favorite.setImageResource(R.drawable.ic_toggle_star);
        } else {
            favorite.setImageResource(R.drawable.ic_toggle_star_outline_red);
        }


        return convertView;
    }
}
