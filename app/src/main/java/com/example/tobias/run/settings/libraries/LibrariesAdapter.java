package com.example.tobias.run.settings.libraries;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tobias.run.R;
import com.example.tobias.run.data.Library;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Recycler View in LibraryActivityView
 */

public class LibrariesAdapter extends RecyclerView.Adapter<LibrariesAdapter.ViewHolder> {

    private List<Library> libraries = new ArrayList<>();
    private onClickListener listener;

    public LibrariesAdapter(List<Library> libraries, onClickListener listener) {
        this.libraries = libraries;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Library library = libraries.get(position);
        holder.name.setText(library.getName());
        holder.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWebsiteButtonClick(library);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libraries.size();
    }

    public interface onClickListener {
        void onWebsiteButtonClick(Library library);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        private Button website;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.library_listitem_name);
            website = itemView.findViewById(R.id.library_listitem_website);
        }
    }
}
