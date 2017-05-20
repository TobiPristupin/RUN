package com.example.tobias.run.historyscreen;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.helpers.DateManager;

import java.util.ArrayList;

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
        String unit = sharedPref.getString("distance_unit", null);

        distanceView.setText(DateManager.distanceToString(currentItem.getDistance(), unit));
    }

    private void setTimeText(final TrackedRun currentItem){
        TextView timeView = (TextView) rootView.findViewById(R.id.time_text);
        long currentItemTime = currentItem.getTime();
        timeView.setText(DateManager.timeToString(currentItemTime));
    }

    private void setDateText(final TrackedRun currentItem){
        TextView dateView = (TextView) rootView.findViewById(R.id.date_text);
        dateView.setText(DateManager.dateToString(currentItem.getDate()));
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

