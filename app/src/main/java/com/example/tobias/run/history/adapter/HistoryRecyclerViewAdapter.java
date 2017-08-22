package com.example.tobias.run.history.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.example.tobias.run.database.TrackedRun;
import com.example.tobias.run.utils.ConversionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for custom RecyclerView in History Fragment.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {

    private ArrayList<TrackedRun> trackedRuns;
    private Context context;
    private View rootView;

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public RatingBar ratingBar;
        public TextView distance;
        public TextView time;
        public TextView date;

        public HistoryViewHolder(View view){
            super(view);
            ratingBar = (RatingBar) view.findViewById(R.id.list_item_rating_bar);
            distance = (TextView) view.findViewById(R.id.list_item_distance_text);
            time = (TextView) view.findViewById(R.id.list_item_time_text);
            date = (TextView) view.findViewById(R.id.list_item_date_text);
        }
    }

    public HistoryRecyclerViewAdapter(Context context, ArrayList<TrackedRun> trackedRuns){
        this.trackedRuns = trackedRuns;
        this.context = context;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        TrackedRun trackedRun = trackedRuns.get(position);
        holder.ratingBar.setRating(trackedRun.getRating());
        holder.date.setText(ConversionManager.dateToString(trackedRun.getDate()));
        holder.time.setText(ConversionManager.timeToString(trackedRun.getTime()));
        setDistanceText(trackedRun, holder);
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

