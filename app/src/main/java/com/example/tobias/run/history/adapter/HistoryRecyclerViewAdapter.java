package com.example.tobias.run.history.adapter;


import android.content.Context;
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
import com.example.tobias.run.data.SharedPreferenceManager;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.data.TrackedRun;
import com.example.tobias.run.utils.ConversionManager;
import com.example.tobias.run.utils.TrackedRunUtils;

import java.util.ArrayList;
import java.util.List;

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
    private OnItemClicked clickListener;
    private SharedPreferenceRepository preferenceManager;

    public HistoryRecyclerViewAdapter(Context context, OnItemClicked clickListener){
        this.context = context;
        this.clickListener = clickListener;
        preferenceManager = new SharedPreferenceManager(context);
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
        holder.distance.setText(TrackedRunUtils.getDistanceText(trackedRun, preferenceManager));
        holder.pace.setText(TrackedRunUtils.getPaceText(trackedRun, preferenceManager));

        if (isSelected(holder.getAdapterPosition())){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
        } else {
            holder.layout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
    }

    /**
     * Called from view to update data set. Uses diffUtil to calculate view updates
     * @param newList
     */
    public void updateItems(List<TrackedRun> newList){
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
            ratingBar = (RatingBar) view.findViewById(R.id.history_list_item_rating_bar);
            distance = (TextView) view.findViewById(R.id.history_list_item_distance_text);
            time = (TextView) view.findViewById(R.id.history_list_item_time_text);
            date = (TextView) view.findViewById(R.id.history_list_item_date_text);
            pace = (TextView) view.findViewById(R.id.history_list_item_pace_text);
            layout = view.findViewById(R.id.history_list_item_layout);

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

