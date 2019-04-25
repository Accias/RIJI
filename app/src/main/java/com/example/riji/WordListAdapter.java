package com.example.riji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private List<BulletPoint> mBulletPoints;
    private final LayoutInflater mInflater;

    public WordListAdapter(Context context,
                           List<BulletPoint> bulletList) {
        mInflater = LayoutInflater.from(context);
        this.mBulletPoints = bulletList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        return new WordViewHolder(mItemView, this);
    }

    void setBulletPoints(List<BulletPoint> bulletPoints){
        mBulletPoints = bulletPoints;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        String mCurrent = mBulletPoints.get(position).getNote();
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mBulletPoints.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder{
        public final TextView wordItemView;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);
            this.mAdapter = adapter;
           // itemView.setOnClickListener(this);
        }

       /* @Override
        public void onClick(View v) {
         implements View.OnClickListener
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mBulletPoints.
            String element = mBulletPoints.get(mPosition).getNote();
            // Change the word in the mBulletPoints.
            mBulletPoints.set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }*/
    }
}