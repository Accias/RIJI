package com.example.riji;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.BulletPoint_related.BulletPoint;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private List<BulletPoint> mBulletPoints;
    private final LayoutInflater mInflater;
    private onNoteListener monNoteListener;

    public WordListAdapter(Context context,
                           List<BulletPoint> bulletList, onNoteListener onNoteListener) {
        mInflater = LayoutInflater.from(context);
        this.mBulletPoints = bulletList;
        this.monNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        return new WordViewHolder(mItemView, this, monNoteListener);
    }

    void setBulletPoints(List<BulletPoint> bulletPoints){
        mBulletPoints = bulletPoints;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        String mCurrent = mBulletPoints.get(position).getSymbol()+" "+mBulletPoints.get(position).getNote();
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mBulletPoints.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public final TextView wordItemView;
        public final WordListAdapter mAdapter;

        onNoteListener onNoteListener;
        public WordViewHolder(View itemView, WordListAdapter adapter, onNoteListener onNoteListener) {
            super(itemView);
            this.mAdapter = adapter;
            wordItemView = itemView.findViewById(R.id.word);
            this.onNoteListener = onNoteListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
            return false;
        }
    }

    public interface onNoteListener{
        void onNoteClick (int position);

    }
}