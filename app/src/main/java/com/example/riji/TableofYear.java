package com.example.riji;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Adapters.YearListAdapter;
import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointViewModel;
import com.example.riji.HandlerThreads.WorkerThreadTableOfYears;
import com.example.riji.Year_related.Year;
import com.example.riji.Year_related.YearViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TableofYear extends AppCompatActivity implements WorkerThreadTableOfYears.Callback {

    float x1, x2, y1, y2;
    private final List<Year> mYear = new ArrayList<>();
    private YearListAdapter mAdapter;
    private WorkerThreadTableOfYears mWorkerThread;
    YearViewModel mYViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableof_year);

        final Database rijiDatabase = Database.getDatabase(this);

        // Get a handle to the RecyclerView.
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new YearListAdapter(this, mYear);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mYViewModel = ViewModelProviders.of(this).get(YearViewModel.class);

        final Button addYear = findViewById(R.id.addYear);
        addYear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               /* ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0,
                        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Database.newYear(rijiDatabase);
                    }
                });*/
            }
        });

        mWorkerThread = new WorkerThreadTableOfYears(new Handler(), this, this);
        mWorkerThread.start();
        mWorkerThread.prepareHandlerYears();
        mWorkerThread.queueYears();
    }

    public void tableToday(View view) {
        startActivity(new Intent(TableofYear.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

    /*public void twentyNineteen(View view) {
        startActivity(new Intent(TableofYear.this, YearActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }*/

    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if (x1 > x2) {
                    Intent i = new Intent(TableofYear.this, YearActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                break;
        }
        return false;
    }

    @Override
    public void onYearsFound(LiveData<List<Year>> years) {
        years.observe(this, new Observer<List<Year>>() {
            @Override
            public void onChanged(@Nullable List<Year> years) {
                // Update the cached copy of days
                mAdapter.setYears(years);
            }
        });

    }

    public void generateNewYear(View v)
    {
        Button yearButton = findViewById(R.id.firstyear);
        String mYear = yearButton.getText().toString();
        int year = Integer.parseInt(mYear);
        //the number button
        mYViewModel.insert(new Year(year));

        Button newButton = new Button(this);
        Typeface dosisRegular = ResourcesCompat.getFont(this, R.font.dosis);
        newButton.setTypeface(dosisRegular);
        newButton.setText("2020");
        newButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        newButton.setTextSize(60);
        LinearLayout ll = findViewById(R.id.two);
        ll.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        //findViewById(R.id.two);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams ls = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


        //the image button
        ImageButton newImage = new ImageButton(this);
        newImage.setBackgroundResource(R.mipmap.mousy);
        newImage.setPadding(60, 0,60, 0 );
        ll.addView(newButton, lp); ll.addView(newImage, ls);

    }

    public void imageButton(int i)
    {
        ImageButton newImage = new ImageButton(this);
        int remainder = i%12;
        switch (remainder){
            case 0:
                newImage.setBackgroundResource(R.mipmap.monkey);
                break;
            case 1:
                newImage.setBackgroundResource(R.mipmap.chicken);
                break;
            case 2:
                newImage.setBackgroundResource(R.mipmap.doggy);
                break;
            case 3:
                newImage.setBackgroundResource(R.mipmap.piggy);
                break;
            case 4:
                newImage.setBackgroundResource(R.mipmap.mousy);
                break;
            case 5:
                newImage.setBackgroundResource(R.mipmap.ox);
                break;
            case 6:
                newImage.setBackgroundResource(R.mipmap.tiger);
                break;
            case 7:
                newImage.setBackgroundResource(R.mipmap.bunny);
                break;
            case 8:
                newImage.setBackgroundResource(R.mipmap.dragon);
                break;
            case 9:
                newImage.setBackgroundResource(R.mipmap.snake);
                break;
            case 10:
                newImage.setBackgroundResource(R.mipmap.horsy);
                break;
            case 11:
                newImage.setBackgroundResource(R.mipmap.sheep);
                break;
        }
    }
}
