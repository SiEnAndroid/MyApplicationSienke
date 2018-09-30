package com.example.administrator.thinker_soft.patrol_inspection.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.patrol_inspection.model.PatrolListItem;

import java.util.ArrayList;

public class PatrolAdapter extends RecyclerView.Adapter<PatrolAdapter.PatrolViewHolder> {
    private ArrayList<PatrolListItem> listItems;
    private onRecyclerItemClickListener itemClickListener;
    private PatrolViewHolder viewHolder;

    public PatrolAdapter(ArrayList<PatrolListItem> listItems, onRecyclerItemClickListener itemClickListener) {
        this.listItems = listItems;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public PatrolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View convetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patrol_list_item, parent, false);
        // 实例化viewholder
        viewHolder = new PatrolViewHolder(convetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PatrolViewHolder holder, int position) {
        PatrolListItem item = listItems.get(position);
        holder.patrolTitle.setText(item.getTitle());
        holder.patrolDate.setText(item.getDate());
        holder.patrolState.setText(item.getState());
        holder.itemView.setTag(item);
    }


    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }


    public interface onRecyclerItemClickListener {
        void OnItemClick(View v, int position);
    }

    public class PatrolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView patrolTitle;
        private TextView patrolDate;
        private TextView patrolState;

        public PatrolViewHolder(View itemView) {
            super(itemView);
            patrolTitle = (TextView) itemView.findViewById(R.id.patrol_title);
            patrolDate = (TextView) itemView.findViewById(R.id.patrol_date);
            patrolState = (TextView) itemView.findViewById(R.id.patrol_state);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.OnItemClick(v, getAdapterPosition());
            }
        }
    }
}