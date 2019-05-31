package com.example.riji.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.Day_related.Day;
import com.example.riji.MainActivity;
import com.example.riji.R;
import com.example.riji.SearchActivity;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ResultViewHolder> {
    private final LayoutInflater mInflater;
    private List<BulletPoint> mBulletPoints;
    private List<Day> mDays;
    private Context mSearchActivity;

    public SearchListAdapter(Context context,
                             List<BulletPoint> bulletList, List<Day> days) {
        mInflater = LayoutInflater.from(context);
        this.mBulletPoints = bulletList;
        mSearchActivity = context;
        mDays = days;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                               int viewType) {
        //new xml for search results
        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        return new ResultViewHolder(mItemView, this);
    }

    public void setBulletPoints(List<BulletPoint> bulletPoints) {
        mBulletPoints = bulletPoints;
        notifyDataSetChanged();
    }

    public void setDays(List<Day> days) {
        mDays = days;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Day day = mDays.get(position);
        BulletPoint bp = mBulletPoints.get(position);
        String mCurrent = day.getMonth() + "/" + day.getDay() + "/" + day.getYear() + "\n" + bp.getSymbol() + " " + bp.getNote();
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mBulletPoints.size();
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }

    class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView wordItemView;
        final SearchListAdapter mAdapter;

        onNoteListener onNoteListener;

        ResultViewHolder(View itemView, SearchListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            wordItemView = itemView.findViewById(R.id.word);
            itemView.setOnClickListener(this);
        }

        //change on long click to on click
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mDays.
            Day day1 = mDays.get(mPosition);
            //insert year and month data to be transfered to MonthActivity class
            Bundle bund = new Bundle();
            bund.putInt("year", day1.getYear());
            bund.putInt("month", day1.getMonth());
            bund.putInt("day", day1.getDay());

            //switch activities
            Intent j = new Intent(mSearchActivity, MainActivity.class);
            j.putExtras(bund);
            mSearchActivity.startActivity(j);
            if (mSearchActivity instanceof SearchActivity) {
                ((Activity) mSearchActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }
    }
}