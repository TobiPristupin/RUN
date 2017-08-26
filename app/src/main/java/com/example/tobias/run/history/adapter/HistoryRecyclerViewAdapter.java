package com.example.tobias.run.history.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.database.FirebaseDatabaseManager;
import com.example.tobias.run.database.TrackedRun;
import com.example.tobias.run.utils.ConversionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for custom RecyclerView in History Fragment.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {

    public interface OnOverflowButtonListener {
        void onDeleteClick(TrackedRun tr);
        void onEditClick(TrackedRun tr);
    }

    private ArrayList<TrackedRun> trackedRuns;
    private Context context;
    private OnOverflowButtonListener listener;


    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        public RatingBar ratingBar;
        public TextView distance;
        public TextView time;
        public TextView date;
        public ImageView overflowIcon;

        public HistoryViewHolder(View view){
            super(view);
            ratingBar = (RatingBar) view.findViewById(R.id.list_item_rating_bar);
            distance = (TextView) view.findViewById(R.id.list_item_distance_text);
            time = (TextView) view.findViewById(R.id.list_item_time_text);
            date = (TextView) view.findViewById(R.id.list_item_date_text);
            overflowIcon = (ImageButton)  view.findViewById(R.id.list_item_overflow_icon);
        }
    }

    public HistoryRecyclerViewAdapter(Context context, ArrayList<TrackedRun> trackedRuns, OnOverflowButtonListener listener){
        this.trackedRuns = trackedRuns;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, final int position) {
        TrackedRun trackedRun = trackedRuns.get(position);
        holder.ratingBar.setRating(trackedRun.getRating());
        holder.date.setText(ConversionManager.dateToString(trackedRun.getDate()));
        holder.time.setText(ConversionManager.timeToString(trackedRun.getTime()));
        setDistanceText(trackedRun, holder);
        holder.overflowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder, position);
            }
        });
    }

    private void showPopupMenu(final HistoryViewHolder holder, final int position){
        final PopupMenu popupMenu = new PopupMenu(context, holder.overflowIcon);
        popupMenu.getMenuInflater().inflate(R.menu.list_item_overflow, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem){
                switch (menuItem.getItemId()){
                    case R.id.menu_overflow_delete_button :
                        listener.onDeleteClick(trackedRuns.get(position));
                        break;
                    case R.id.menu_overflow_edit_button :
                        listener.onEditClick(trackedRuns.get(position));
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void setDistanceText(TrackedRun currentItem, HistoryViewHolder holder){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_key), Context.MODE_PRIVATE);
        String unit = sharedPref.getString(context.getString(R.string.preference_distance_unit_key), null);
        if (unit.equals("km")){
            holder.distance.setText(ConversionManager.distanceToString(currentItem.getDistanceKilometres(), unit));
        } else {
            holder.distance.setText(ConversionManager.distanceToString(currentItem.getDistanceMiles(), unit));
        }
    }

    @Override
    public int getItemCount() {
        return trackedRuns.size();
    }
}

