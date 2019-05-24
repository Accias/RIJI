package com.example.riji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Month_related.Month;
import com.example.riji.Year_related.Year;

import java.util.List;

public class YearListAdapter extends RecyclerView.Adapter<YearListAdapter.YearViewHolder> {
    private List<Year> mYear;
    private final LayoutInflater mInflater;
    private onNoteListener monNoteListener;

    YearListAdapter(Context context,
                     List<Year> yearlist) {
        mInflater = LayoutInflater.from(context);
        this.mYear = yearlist;
    }

    @NonNull
    @Override
    public YearViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.yearlist_item,
                parent, false);
        return new YearListAdapter.YearViewHolder(mItemView, this, monNoteListener);
    }

    void setYear(List<Year> year) {
        mYear = year;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull YearViewHolder holder, int position) {
        int mCurrent = mYear.get(position).getYear();
        holder.buttonView.setText(mCurrent);

    }

    @Override
    public int getItemCount() {
        return mYear.size();
    }

    class YearViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        final Button buttonView;
        final YearListAdapter mAdapter;

        onNoteListener onNoteListener;

        YearViewHolder(View itemView, YearListAdapter adapter, onNoteListener onNoteListener) {
            super(itemView);
            this.mAdapter = adapter;
            buttonView = itemView.findViewById(R.id.firstyear);
            this.onNoteListener = onNoteListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
            return false;
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }
}