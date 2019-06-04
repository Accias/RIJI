package com.example.riji.Adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointRepository;
import com.example.riji.Database;
import com.example.riji.R;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private final LayoutInflater mInflater;
    private List<BulletPoint> mBulletPoints;
    private onNoteListener monNoteListener;
    Database db;

    public WordListAdapter(Context context,
                           List<BulletPoint> bulletList, onNoteListener onNoteListener, Database db) {
        mInflater = LayoutInflater.from(context);
        this.mBulletPoints = bulletList;
        this.monNoteListener = onNoteListener;
        this.db = db;
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
        if(mBulletPoints.get(position).getBulletType()==5){
            holder.wordItemView.setPaintFlags( holder.wordItemView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return mBulletPoints.size();
    }

    public void deleteBP(int position, Application application) {
        BulletPointRepository mBPRepository = new BulletPointRepository(application);
        mBPRepository.deleteBulletPoint(mBulletPoints.get(position));
    }

    public interface onNoteListener {
        void onNoteClick(int position);

    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        final TextView wordItemView;
        final WordListAdapter mAdapter;

        onNoteListener onNoteListener;

        WordViewHolder(View itemView, WordListAdapter adapter, onNoteListener onNoteListener) {
            super(itemView);
            this.mAdapter = adapter;
            wordItemView = itemView.findViewById(R.id.word);
            this.onNoteListener = onNoteListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
            return false;
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            final BulletPoint bp = mBulletPoints.get(pos);
            int bulletType = bp.getBulletType();
            if (bulletType == 5) {
                LinearLayout lin = (LinearLayout) v;
                TextView text = v.findViewById(R.id.word);
                text.setPaintFlags(text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                bp.setBulletType(0);
            } else {
                bp.setBulletType(bulletType + 1);
            }
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0,
                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.getBulletPointDAO().updateBulletPoint(bp);
                }
            });
        }
    }
}