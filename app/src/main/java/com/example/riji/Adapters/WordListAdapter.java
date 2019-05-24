package com.example.riji.Adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointRepository;
import com.example.riji.R;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private final LayoutInflater mInflater;
    private List<BulletPoint> mBulletPoints;
    private onNoteListener monNoteListener;
    private BulletPointRepository mBPRepository;

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

    public void setBulletPoints(List<BulletPoint> bulletPoints) {
        mBulletPoints = bulletPoints;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        String mCurrent = mBulletPoints.get(position).getSymbol() + " " + mBulletPoints.get(position).getNote();
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mBulletPoints.size();
    }

    public void deleteBP(int position, Application application) {
        mBPRepository = new BulletPointRepository(application);
        mBPRepository.deleteBulletPoint(mBulletPoints.get(position));
    }

    public interface onNoteListener {
        void onNoteClick(int position);

    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        final TextView wordItemView;
        final WordListAdapter mAdapter;

        onNoteListener onNoteListener;

        WordViewHolder(View itemView, WordListAdapter adapter, onNoteListener onNoteListener) {
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
}