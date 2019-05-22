package com.example.riji;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riji.Month_related.Month;

import java.util.List;

public class MonthListAdapter extends RecyclerView.Adapter<MonthListAdapter.WordViewHolder> {
    private List<Month> mMonth;
    private final LayoutInflater mInflater;
    private onNoteListener monNoteListener;

    MonthListAdapter(Context context, List<Month> monthlist) {
        mInflater = LayoutInflater.from(context);
        this.mMonth = monthlist;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.monthlistadapter,
                parent, false);
        return new WordViewHolder(mItemView, this, monNoteListener);
    }

    void setMonth(List<Month> month) {
        mMonth = month;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        int mCurrent =  mMonth.get(position).getMonth();
        holder.buttonView.setText(months(mCurrent));
    }

    public String months(int day)
    {
        String name = new String();
        switch (day)
        {
            case 1:
                name ="January";
                break;
            case 2:
                name = "Feburary";
                break;
            case 3:
                name = "March";
                break;
            case 4:
                name = "April";
                break;
            case 5:
                name = "May";
                break;
            case 6:
                name = "June";
                break;
            case 7:
                name = "July";
                break;
            case 8:
                name = "August";
                break;
            case 9:
                name = "September";
                break;
            case 10:
                name = "October";
                break;
            case 11:
                name = "November";
                break;
            case 12:
                name = "December";
                break;
        }return name;
    }

    @Override
    public int getItemCount() {
        return mMonth.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        final Button buttonView;
        final MonthListAdapter mAdapter;

        onNoteListener onNoteListener;

        WordViewHolder(View itemView, MonthListAdapter adapter, onNoteListener onNoteListener) {
            super(itemView);
            this.mAdapter = adapter;
            buttonView = itemView.findViewById(R.id.buttonMonth);
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