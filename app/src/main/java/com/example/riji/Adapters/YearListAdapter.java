package com.example.riji.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.MonthActivity;
import com.example.riji.R;
import com.example.riji.YearActivity;
import com.example.riji.Year_related.Year;

import java.util.List;

public class YearListAdapter extends RecyclerView.Adapter<YearListAdapter.YearViewHolder> {
    private List<Year> mYear;
    private final LayoutInflater mInflater;
    private Context mTableActivity;

    public YearListAdapter(Context context,
                           List<Year> yearlist) {
        mInflater = LayoutInflater.from(context);
        this.mYear = yearlist;
        mTableActivity = context;
    }

    @NonNull
    @Override
    public YearViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.yearlist_item,
                parent, false);
        return new YearListAdapter.YearViewHolder(mItemView, this);
    }

    public void setYears(List<Year> year) {
        mYear = year;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull YearViewHolder holder, int position) {
        String mCurrent =Integer.toString(mYear.get(position).getYear());
        holder.buttonView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mYear.size();
    }

    class YearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final Button buttonView;
        final YearListAdapter mAdapter;
        final ImageButton zodiac;

        YearViewHolder(View itemView, YearListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            buttonView = itemView.findViewById(R.id.year1);
            zodiac = itemView.findViewById(R.id.zodiac);
            buttonView.setOnClickListener(this);

            String mYear = buttonView.getText().toString();
            int year = Integer.parseInt(mYear);

            //the number button
            int remainder = year%12;
            switch (remainder){
                case 0:
                    zodiac.setBackgroundResource(R.mipmap.monkey);
                    break;
                case 1:
                    zodiac.setBackgroundResource(R.mipmap.chicken);
                    break;
                case 2:
                    zodiac.setBackgroundResource(R.mipmap.doggy);
                    break;
                case 3:
                    zodiac.setBackgroundResource(R.mipmap.piggy);
                    break;
                case 4:
                    zodiac.setBackgroundResource(R.mipmap.mousy);
                    break;
                case 5:
                    zodiac.setBackgroundResource(R.mipmap.ox);
                    break;
                case 6:
                    zodiac.setBackgroundResource(R.mipmap.tiger);
                    break;
                case 7:
                    zodiac.setBackgroundResource(R.mipmap.bunny);
                    break;
                case 8:
                    zodiac.setBackgroundResource(R.mipmap.dragon);
                    break;
                case 9:
                    zodiac.setBackgroundResource(R.mipmap.snake);
                    break;
                case 10:
                    zodiac.setBackgroundResource(R.mipmap.horsy);
                    break;
                case 11:
                    zodiac.setBackgroundResource(R.mipmap.sheep);
                    break;
            }
        }

        public void onClick(View v) {

            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mDays.
            int year1 = mYear.get(mPosition).getYear();
            //insert year and month data to be transfered to MonthActivity class
            Bundle bund = new Bundle();
            bund.putInt("year", year1);

            //switch activities
            Intent j = new Intent(mTableActivity, YearActivity.class);
            j.putExtras(bund);
            mTableActivity.startActivity(j);
            if (mTableActivity instanceof YearActivity) {
                ((Activity) mTableActivity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ((Activity) mTableActivity).finish();
            }
        }
    }

}