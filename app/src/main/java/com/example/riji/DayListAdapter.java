package com.example.riji;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayRepository;

import java.util.List;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.DayViewHolder> {

    private List<Day> mDays;
    private LayoutInflater mInflater;
    private Context mMonthActivity;
    private DayRepository mDayRepository;
    private DayListAdapter.onNoteListener monNoteListener;

    DayListAdapter(Context context, List<Day> dayList, Application application, DayListAdapter.onNoteListener onNoteListener) {
        mInflater = LayoutInflater.from(context);
        this.mDays = dayList;
        mMonthActivity = context;
        mDayRepository = new DayRepository(application);
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public DayListAdapter.DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View mItemView = mInflater.inflate(R.layout.daylist_item,
                parent, false);
        return new DayListAdapter.DayViewHolder((RelativeLayout) mItemView, this, monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final DayListAdapter.DayViewHolder holder, int position) {
        //implement get string method in Day class
        String mCurrent = mDays.get(position).getNote();
        String mDate = mDays.get(position).getDay() + "";
        holder.editText.setText(mCurrent);
        holder.button.setText(mDate);

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = holder.editText.getText().toString();
                int position= holder.getLayoutPosition();
                Day day1 = mDays.get(position);
                if(!note.equals("") &&!note.equals(day1.getNote())){
                    day1.setNote(note);
                    mDays.set(position,day1);
                    mDayRepository.updateDay(day1);
                }
            }
        });
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
        final Button saveButton;
        final DayListAdapter mAdapter;

        DayListAdapter.onNoteListener onNoteListener;

        DayViewHolder(RelativeLayout itemView, DayListAdapter adapter, DayListAdapter.onNoteListener onNoteListener) {
            super(itemView);
            //   layout = itemView.findViewById(R.id.lay);
            saveButton =itemView.findViewById(R.id.save);
            button = itemView.findViewById(R.id.date);
            editText = itemView.findViewById(R.id.Day1Sum);
            this.mAdapter = adapter;
            this.onNoteListener = onNoteListener;
            button.setOnClickListener(this);
            //saveButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onNoteListener.onNoteClick(getAdapterPosition());
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
                ((Activity) mMonthActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ((Activity) mMonthActivity).finish();
            }
        }

    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }
}