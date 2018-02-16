package com.example.tobias.run.ui.history.adapter;


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
import com.example.tobias.run.data.interfaces.Repository;
import com.example.tobias.run.data.manager.FirebaseRepository;
import com.example.tobias.run.data.manager.FirebaseSettingsSingleton;
import com.example.tobias.run.data.model.Distance;
import com.example.tobias.run.data.model.Run;
import com.example.tobias.run.utils.RunUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter for custom RecyclerView in History Fragment.
 */
public class HistoryRecyclerViewAdapter extends SelectableAdapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {

    private ArrayList<Run> runs = new ArrayList<>();
    private Context context;
    private OnItemClicked clickListener;

    public HistoryRecyclerViewAdapter(Context context, OnItemClicked clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {
        final Run run = runs.get(holder.getAdapterPosition());

        Distance.Unit unit = getDistanceUnit();

        holder.ratingBar.setRating(run.getRating());
        holder.date.setText(RunUtils.dateToString(run.getDate()));
        holder.time.setText(RunUtils.timeToString(run.getTime(), true));
        holder.distance.setText(RunUtils.distanceToString(run.getDistance(unit), unit));
        holder.pace.setText(RunUtils.paceToString(run.getPace(unit), unit));

        if (isSelected(holder.getAdapterPosition())){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
        } else {
            holder.layout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return this.runs.size();
    }

    /**
     * Called from view to update data set. Uses diffUtil to calculate view updates
     * @param newList
     */
    public void updateItems(List<Run> newList){
        final DiffCallback diff = new DiffCallback(newList, this.runs);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diff);

        this.runs.clear();
        Collections.sort(newList);
        this.runs.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    private Distance.Unit getDistanceUnit(){
        return FirebaseSettingsSingleton.getInstance().getDistanceUnit();
    }

    public interface OnItemClicked {
        void onClick(int position);
        boolean onLongClick(int position);
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
            ratingBar = view.findViewById(R.id.history_list_item_rating_bar);
            distance = view.findViewById(R.id.history_list_item_distance_text);
            time = view.findViewById(R.id.history_list_item_time_text);
            date = view.findViewById(R.id.history_list_item_date_text);
            pace = view.findViewById(R.id.history_list_item_pace_text);
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

