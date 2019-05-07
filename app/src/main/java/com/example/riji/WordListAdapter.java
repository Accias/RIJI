package com.example.riji;

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
        String mCurrent = mBulletPoints.get(position).getSymbol()+" "+mBulletPoints.get(position).getNote();
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mBulletPoints.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public final TextView wordItemView;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);
            this.mAdapter = adapter;

           /* wordItemView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v)
                    {

                        int mPosition = getLayoutPosition();

                        String element = mBulletPoints.get(mPosition).getNote();

                        mBulletPoints.set(mPosition, "Clicked! " + element);

                        mAdapter.notifyDataSetChanged();
                    }
            });*/

        }

        @Override
        public boolean onLongClick(View v) {


            int mPosition = getLayoutPosition();

            BulletPoint element = mBulletPoints.get(mPosition);


            //mBulletPoints.set(mPosition,element);


            mAdapter.notifyDataSetChanged();

            return false;
        }
    }
}