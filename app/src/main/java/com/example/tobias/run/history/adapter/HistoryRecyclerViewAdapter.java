package com.example.tobias.run.history.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.DiffCallback;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.utils.ConversionManager;

import java.util.ArrayList;

/**
 * Adapter for custom RecyclerView in History Fragment.
 */
public class HistoryRecyclerViewAdapter extends SelectableAdapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {

    public interface OnItemClicked {
        void onClick(int position);
        boolean onLongClick(int position);
    }

    private ArrayList<TrackedRun> trackedRuns = new ArrayList<>();
    private Context context;
    private SharedPreferences sharedPref;
    private OnItemClicked clickListener;

    public HistoryRecyclerViewAdapter(Context context, ArrayList<TrackedRun> trackedRuns, OnItemClicked clickListener){
        /*Creating new ArrayList and adding instead of copying reference because changes made on adapter's trackedRuns should not affect
        HistoryFragment trackedRuns.*/
        this.trackedRuns.addAll(trackedRuns);
        this.context = context;
        this.clickListener = clickListener;
        this.sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_key), Context.MODE_PRIVATE);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {
        final TrackedRun trackedRun = trackedRuns.get(holder.getAdapterPosition());
        holder.ratingBar.setRating(trackedRun.getRating());
        holder.date.setText(ConversionManager.dateToString(trackedRun.getDate()));
        holder.time.setText(ConversionManager.timeToString(trackedRun.getTime()));
        setDistanceText(trackedRun, holder);
        setPaceText(trackedRun, holder);

        if (isSelected(holder.getAdapterPosition())){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
        } else {
            holder.layout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
    }

    public void updateItems(ArrayList<TrackedRun> newList){
        final DiffCallback diff = new DiffCallback(newList, this.trackedRuns);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diff);

        this.trackedRuns.clear();
        this.trackedRuns.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
    
    public boolean isDatasetEmpty(){
        return getItemCount() == 0;
    }

    public ArrayList<TrackedRun> getDataset(){
        return this.trackedRuns;
    }

    private void setDistanceText(TrackedRun currentItem, HistoryViewHolder holder){
        String unit = sharedPref.getString(context.getString(R.string.preference_distance_unit_key), null);
        if (unit.equals("km")){
            holder.distance.setText(ConversionManager.distanceToString(currentItem.getDistanceKilometres(), unit));
        } else {
            holder.distance.setText(ConversionManager.distanceToString(currentItem.getDistanceMiles(), unit));
        }
    }

    private void setPaceText(TrackedRun currentItem, HistoryViewHolder holder){
        String unit = sharedPref.getString(context.getString(R.string.preference_distance_unit_key), null);
        if (unit.equals("km")){
            holder.pace.setText(ConversionManager.paceToString(currentItem.getKmPace(), "km"));
        } else {
            holder.pace.setText(ConversionManager.paceToString(currentItem.getMilePace(), "mi"));
        }
    }



    @Override
    public int getItemCount() {
        return this.trackedRuns.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public RatingBar ratingBar;
        public TextView distance;
        public TextView time;
        public TextView date;
        public TextView pace;
        public ConstraintLayout layout;

        public HistoryViewHolder(View view){
            super(view);
            ratingBar = (RatingBar) view.findViewById(R.id.list_item_rating_bar);
            distance = (TextView) view.findViewById(R.id.list_item_distance_text);
            time = (TextView) view.findViewById(R.id.list_item_time_text);
            date = (TextView) view.findViewById(R.id.list_item_date_text);
            pace = (TextView) view.findViewById(R.id.list_item_pace_text);
            layout = view.findViewById(R.id.list_item_layout);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return clickListener.onLongClick(getAdapterPosition());
        }


    }


}

