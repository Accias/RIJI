package com.example.riji.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.MonthActivity;
import com.example.riji.Month_related.Month;
import com.example.riji.R;
import com.example.riji.YearActivity;

import java.util.List;

public class MonthListAdapter extends RecyclerView.Adapter<MonthListAdapter.MonthViewHolder> {
    private final LayoutInflater mInflater;
    private List<Month> mMonth;
    private Context mYearActivity;

    public MonthListAdapter(Context context, List<Month> monthlist) {
        mInflater = LayoutInflater.from(context);
        this.mMonth = monthlist;
        mYearActivity = context;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View mItemView = mInflater.inflate(R.layout.monthlist_item,
                parent, false);
        return new MonthViewHolder(mItemView, this);
    }

    public void setMonth(List<Month> month) {
        mMonth = month;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        int mCurrent = mMonth.get(position).getMonth();
        holder.buttonView.setText(months(mCurrent));
    }

    public String months(int day) {
        String name = "";
        switch (day) {
            case 1:
                name = "January";
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
        }
        return name;
    }

    @Override
    public int getItemCount() {
        return mMonth.size();
    }

    class MonthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final Button buttonView;
        final MonthListAdapter mAdapter;

        MonthViewHolder(View itemView, MonthListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            buttonView = itemView.findViewById(R.id.buttonMonth);
            buttonView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mDays.
            int month1 = mMonth.get(mPosition).getMonth();
            int year1 = mMonth.get(mPosition).getYear();
            //insert year and month data to be transfered to MonthActivity class
            Bundle bund = new Bundle();
            bund.putInt("year", year1);
            bund.putInt("month", month1);

            //switch activities
            Intent j = new Intent(mYearActivity, MonthActivity.class);
            j.putExtras(bund);
            mYearActivity.startActivity(j);
            if (mYearActivity instanceof YearActivity) {
                ((Activity) mYearActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ((Activity) mYearActivity).finish();
            }
        }
    }
}