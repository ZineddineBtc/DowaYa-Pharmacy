package com.example.dowaya_pharmacy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.models.User;

import java.util.List;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.ViewHolder> {

    private List<User> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public UserHistoryAdapter(Context context, List<User> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.name_time_history_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(list.get(position).getUsername());
        holder.timeTV.setText(list.get(position).getSearchHistoryTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV, timeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            timeTV = itemView.findViewById(R.id.timeTV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    User getItem(int id) {
        return list.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
