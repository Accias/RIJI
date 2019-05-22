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
        return new DayListAdapter.DayViewHolder((RelativeLayout) mItemView, this, new MyCustomEditTextListener(), monNoteListener);
    }

    @Override
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
        public final Button saveButton;
        final DayListAdapter mAdapter;
        public MyCustomEditTextListener myCustomEditTextListener;

        DayListAdapter.onNoteListener onNoteListener;

        DayViewHolder(RelativeLayout itemView, DayListAdapter adapter, MyCustomEditTextListener myCustomEditTextListener, DayListAdapter.onNoteListener onNoteListener) {
            super(itemView);
            //   layout = itemView.findViewById(R.id.lay);
            saveButton =itemView.findViewById(R.id.save);
            button = itemView.findViewById(R.id.date);
            editText = itemView.findViewById(R.id.Day1Sum);
            this.mAdapter = adapter;
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.onNoteListener = onNoteListener;
            button.setOnClickListener(this);
            //saveButton.setOnClickListener(this);
            editText.addTextChangedListener(myCustomEditTextListener);
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
                ((Activity) mMonthActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ((Activity) mMonthActivity).finish();
            }
        }

    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            String note = charSequence.toString();
            Day day1 = mDays.get(position);
            day1.setNote(note);
            mDays.set(position, day1);
            mDayRepository.updateDay(day1);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position);

    }
}