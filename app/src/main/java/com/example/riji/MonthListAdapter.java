package com.example.riji;

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

import com.example.riji.Day_related.Day;
import com.example.riji.Month_related.Month;

import java.util.List;

public class MonthListAdapter extends RecyclerView.Adapter<MonthListAdapter.MonthViewHolder> {
    private List<Month> mMonth;
    private final LayoutInflater mInflater;
    private onNoteListener monNoteListener;
    private Context mYearActivity;

    MonthListAdapter(Context context, List<Month> monthlist) {
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
        return new MonthViewHolder(mItemView, this, monNoteListener);
    }

    void setMonth(List<Month> month) {
        mMonth = month;
        notifyDataSetChanged();
    }
   /* @Override
    public void onBindViewHolder(@NonNull DayListAdapter.DayViewHolder holder, int position) {
        //implement get string method in Day class
        String mCurrent = mDays.get(position).getNote();
        String mDate = mDays.get(position).getDay() + "";
        holder.editText.setText(mCurrent);
        holder.button.setText(mDate);

        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mDataset to update
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.editText.setText(mDays.get(holder.getAdapterPosition()).getNote());

    }*/

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
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

    class MonthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final Button buttonView;
        final MonthListAdapter mAdapter;

        onNoteListener onNoteListener;

        MonthViewHolder(View itemView, MonthListAdapter adapter, onNoteListener onNoteListener) {
            super(itemView);
            this.mAdapter = adapter;
            buttonView = itemView.findViewById(R.id.buttonMonth);
            this.onNoteListener = onNoteListener;
            buttonView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            //onNoteListener.onNoteClick(getAdapterPosition());
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mDays.
            // Button button = (Button) v;
            //  String date= (String) button.getText();
            int month1 = mMonth.get(mPosition).getMonth();
            int year1 = mMonth.get(mPosition).getYear();
            //insert year and month data to be transfered to MonthActivity class
            Bundle bund = new Bundle();
            bund.putInt("year", month1);
            bund.putInt("month", year1);

            //switch activities
            Intent j = new Intent(mYearActivity, TableofYear.class);
            j.putExtras(bund);
            mYearActivity.startActivity(j);
            if (mYearActivity instanceof YearActivity) {
                ((Activity) mYearActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ((Activity) mYearActivity).finish();
            }
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }
}