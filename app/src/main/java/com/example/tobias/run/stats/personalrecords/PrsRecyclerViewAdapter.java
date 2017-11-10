package com.example.tobias.run.stats.personalrecords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.PersonalRecord;
import com.example.tobias.run.data.SharedPreferenceManager;
import com.example.tobias.run.data.SharedPreferenceRepository;
import com.example.tobias.run.utils.PersonalRecordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 11/8/2017.
 */

public class PrsRecyclerViewAdapter extends RecyclerView.Adapter<PrsRecyclerViewAdapter.ViewHolder> {

    private List<PersonalRecord> personalRecordList = new ArrayList<>();
    SharedPreferenceRepository sharedPref;
    private Context context;

    public PrsRecyclerViewAdapter(Context context) {
        this.context = context;
        sharedPref = new SharedPreferenceManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View personalRecordView = inflater.inflate(R.layout.prs_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(personalRecordView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PersonalRecord personalRecord = personalRecordList.get(position);

        holder.title.setText(personalRecord.getTitle());

        if (personalRecord.isAchieved()){
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_circle_green));
            holder.value.setText(PersonalRecordUtils.getValueText(personalRecord, sharedPref));
            holder.date.setText(PersonalRecordUtils.getDateText(personalRecord));
            holder.pace.setText(PersonalRecordUtils.getPaceText(personalRecord, sharedPref));
        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_circle_red));
            holder.value.setText("N/A");
            holder.date.setText("N/A");
            holder.pace.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return personalRecordList.size();
    }

    public void updateItems(List<PersonalRecord> personalRecords){
        personalRecordList.clear();
        personalRecordList.addAll(personalRecords);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView title;
        public TextView date;
        public TextView value;
        public TextView pace;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pr_list_item_image);
            title = itemView.findViewById(R.id.pr_list_item_text);
            date = itemView.findViewById(R.id.pr_list_item_date);
            value = itemView.findViewById(R.id.pr_list_item_value);
            pace = itemView.findViewById(R.id.pr_list_item_pace);
        }
    }
}
