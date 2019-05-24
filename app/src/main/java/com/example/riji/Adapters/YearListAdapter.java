package com.example.riji.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.R;
import com.example.riji.Year_related.Year;

import java.util.List;

public class YearListAdapter extends RecyclerView.Adapter<YearListAdapter.WordViewHolder> {
    private final LayoutInflater mInflater;
    private List<Year> mYear;
    private onNoteListener monNoteListener;

    public YearListAdapter(Context context,
                           List<Year> yearlist) {
        mInflater = LayoutInflater.from(context);
        this.mYear = yearlist;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.yearlist_item,
                parent, false);
        return new WordViewHolder(mItemView, this, monNoteListener);
    }

    void setYear(List<Year> year) {
        mYear = year;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        int mCurrent = mYear.get(position).getYear();
        holder.buttonView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mYear.size();
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        final Button buttonView;
        final YearListAdapter mAdapter;

        onNoteListener onNoteListener;

        WordViewHolder(View itemView, YearListAdapter adapter, onNoteListener onNoteListener) {
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
}