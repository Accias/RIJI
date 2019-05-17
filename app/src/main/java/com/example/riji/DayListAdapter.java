package com.example.riji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Day_related.Day;

import java.util.List;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.DayViewHolder> {

    private List<Day> mDays;
    private LayoutInflater mInflater;
    private Context mMonthActivity;

    DayListAdapter(Context context, List<Day> dayList) {
        mInflater = LayoutInflater.from(context);
        this.mDays = dayList;
        mMonthActivity = context;
    }

    @NonNull
    @Override
    public DayListAdapter.DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View mItemView = mInflater.inflate(R.layout.daylist_item,
                parent, false);
        return new DayListAdapter.DayViewHolder((LinearLayout) mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull DayListAdapter.DayViewHolder holder, int position) {

        //implement get string method in Day class
        String mCurrent = mDays.get(position).getNote();
        String mDate = mDays.get(position).getDay() + "";
        holder.editText.setText(mCurrent);
        holder.button.setText(mDate);
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    void setDays(List<Day> days) {
        mDays = days;
        notifyDataSetChanged();
    }

    class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final Button button;
        final EditText editText;
        // public final LinearLayout layout;
        final DayListAdapter mAdapter;
        // public final ArrayList<Button> buttons;

        DayViewHolder(LinearLayout itemView, DayListAdapter adapter) {
            super(itemView);
            //   layout = itemView.findViewById(R.id.lay);
            button = itemView.findViewById(R.id.dateButton);
            editText = itemView.findViewById(R.id.Day1Sum);
            this.mAdapter = adapter;
            //   buttons = new ArrayList<>();

            //  for(int i = 0; i < 10; i++){


            //       //optional: add your buttons to any layout if you want to see them in your screen
            //       RecyclerView.add(button);
            //   }
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mDays.
            // Button button = (Button) v;
            //  String date= (String) button.getText();
            Day day1 = mDays.get(mPosition);
            //insert year and month data to be transfered to MonthActivity class
            Bundle bund = new Bundle();
            bund.putInt("year", day1.getYear());
            bund.putInt("month", day1.getMonth());
            bund.putInt("day", day1.getDay());

            //switch activities
            Intent j = new Intent(mMonthActivity, MainActivity.class);
            j.putExtras(bund);
            mMonthActivity.startActivity(j);
            if (mMonthActivity instanceof MonthActivity) {
                ((Activity) mMonthActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ((Activity) mMonthActivity).finish();
            }
        }
    }
}
