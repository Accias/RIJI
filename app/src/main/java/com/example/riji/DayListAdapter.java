package com.example.riji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Day_related.Day;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.DayViewHolder>{

    private final List<Day> mDays;
    private LayoutInflater mInflater;

    public DayListAdapter(Context context,
                           List<Day> dayList) {
        mInflater = LayoutInflater.from(context);
        this.mDays = dayList;

    }

    @Override
    public DayListAdapter.DayViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.daylist_item,
                parent, false);
        return new DayListAdapter.DayViewHolder((LinearLayout)mItemView, this);
    }

    @Override
    public void onBindViewHolder(DayListAdapter.DayViewHolder holder, int position) {

        //implement get string method in Day class
      //  String mCurrent = mDays.get(position).getNote();
       // String mDate = mDays.get(position).getDay();
        //holder.editText.setText(mCurrent);
       // holder.button.setText(mDate);
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder{
        public final Button button;
        public final EditText editText;
       // public final LinearLayout layout;
        final DayListAdapter mAdapter;
       // public final ArrayList<Button> buttons;

        public DayViewHolder(LinearLayout itemView, DayListAdapter adapter) {
            super(itemView);
         //   layout = itemView.findViewById(R.id.lay);
            button = itemView.findViewById(R.id.dateButton);
            editText = itemView.findViewById(R.id.Day1Sum);
            this.mAdapter = adapter;
         //   buttons = new ArrayList<>();

          //  for(int i = 0; i < 10; i++){

                ;
         //       //optional: add your buttons to any layout if you want to see them in your screen
         //       RecyclerView.add(button);
         //   }
            // itemView.setOnClickListener(this);
        }

       /* @Override
        public void onClick(View v) {
         implements View.OnClickListener
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mDays.
            String element = mDays.get(mPosition).getNote();
            // Change the word in the mDays.
            mDays.set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }*/
    }
}
