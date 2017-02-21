package com.example.tobias.run;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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

    public interface OnOverflowButtonListener {
        void onDeleteClick(TrackedRun tr);
        void onEditClick(TrackedRun tr);
    }


    private Context context;
    private View rootView;
    private OnOverflowButtonListener listener;

    public HistoryListItemAdapter(Context context, ArrayList<TrackedRun> items,
                                  HistoryFragment historyFragment, OnOverflowButtonListener listener){
        super(context, 0, items);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rootView = convertView;
        TrackedRun currentItem = getItem(position);

        if (null == rootView) {
            rootView = inflater.inflate(
                    R.layout.history_list_item,
                    parent,
                    false);
        }


        //UI references
        setDistanceText(currentItem);
        setTimeText(currentItem);
        setDateText(currentItem);
        setRatingText(currentItem);
        setOverflowMenu(currentItem);

        return rootView;
    }

    private void setDistanceText(final TrackedRun currentItem){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_key), Context.MODE_PRIVATE);
        TextView distanceView = (TextView) rootView.findViewById(R.id.distance_text);
        DecimalFormat df = new DecimalFormat("0.00");

        double distance = currentItem.getDistance() / 100; //Convert to km from m
        String distanceText = df.format(distance) + " " + sharedPref.getString("distance_unit", null);

        distanceView.setText(distanceText);
    }

    private void setTimeText(final TrackedRun currentItem){
        TextView timeView = (TextView) rootView.findViewById(R.id.time_text);
        long currentItemTime = currentItem.getTime();
        //Create period to transform time in millis to formatted time.
        Period period = new Period(currentItemTime);

        String timeText = new StringBuilder()
            .append(String.format(Locale.US, "%02d", period.getHours()))
                .append(":")
                .append(String.format(Locale.US, "%02d", period.getMinutes()))
                .append(":")
                .append(String.format(Locale.US, "%02d", period.getSeconds())).toString();

        timeView.setText(timeText);
    }

    private void setDateText(final TrackedRun currentItem){
        TextView dateView = (TextView) rootView.findViewById(R.id.date_text);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("E, e/MMM/YYYY");
        //Unix time is in milli, DateTime is in sec, so multiply by 1000 to convert
        String dateText = formatter.print(new DateTime(currentItem.getDate() * 1000L));
        dateView.setText(dateText);
    }

    private void setRatingText(final TrackedRun currentItem){
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
        ratingBar.setRating(currentItem.getRating());
    }

    private void setOverflowMenu(final TrackedRun currentItem){
        final ImageButton button = (ImageButton) rootView.findViewById(R.id.overflow_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final PopupMenu menu = new PopupMenu(getContext(), button);
                menu.getMenuInflater().inflate(R.menu.list_item_overflow, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.edit_button :
                                listener.onEditClick(currentItem);
                                break;

                            case R.id.delete_button :
                                listener.onDeleteClick(currentItem);
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
}

}

