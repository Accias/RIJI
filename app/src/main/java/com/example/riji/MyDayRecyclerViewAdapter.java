package com.example.riji;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.riji.DayFragment.OnListFragmentInteractionListener;
import com.example.riji.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDayRecyclerViewAdapter extends RecyclerView.Adapter<MyDayRecyclerViewAdapter.ViewHolder> {

    private  List<BulletPoint> mDataset;
    private OnListFragmentInteractionListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bulletpoint;

        public ViewHolder(TextView view) {
            super(view);
            bulletpoint = view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + bulletpoint.getText() + "'";
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyDayRecyclerViewAdapter(List<BulletPoint> items, OnListFragmentInteractionListener listener) {
        mDataset = items;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_day, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bulletpoint.setText(mDataset.get(position).getNote());

        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
