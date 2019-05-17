package com.example.riji;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Day_related.Day;
import com.example.riji.Month_related.Month;

import java.util.ArrayList;
import java.util.List;

public class MonthActivity extends AppCompatActivity implements WorkerThreadMonth.Callback {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    private final List<Day> mDays = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private DayListAdapter mAdapter;
    private Month month1;
    private long month_id;
    Integer year, month;


    private WorkerThreadMonth mWorkerThread;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        monthBackYear();

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new DayListAdapter(this, mDays);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bund = getIntent().getExtras();
        //get the current year and month

        year = bund.getInt("year");
        month = bund.getInt("month");

        mWorkerThread = new WorkerThreadMonth(new Handler(), this, this);
        mWorkerThread.start();
        mWorkerThread.prepareHandlerMonth();
        mWorkerThread.queueMonth(year, month);
    }

    public void monthBackYear() {
        Button backButton = findViewById(R.id.Twenty19);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MonthActivity.this, YearActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

    }

    public void monthToday(View view) {
        startActivity(new Intent(MonthActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

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
                    Intent i = new Intent(MonthActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                if (x1 < x2) {
                    Intent j = new Intent(MonthActivity.this, YearActivity.class);
                    startActivity(j);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
                break;
        }
        return false;
    }

    //bug here, list is null
    @Override
    public void onDayFound(final LiveData<List<Day>> days) {
        days.observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(@Nullable List<Day> days) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setDays(days);
            }
        });
    }

    @Override
    public void onMonthFound(Month month, long month_id) {
        month1 = month;
        this.month_id = month_id;
        mWorkerThread.prepareHandlerDay();
        mWorkerThread.queueDay(year, this.month);
    }
}
