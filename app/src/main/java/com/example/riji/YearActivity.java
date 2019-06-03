package com.example.riji;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Adapters.MonthListAdapter;
import com.example.riji.HandlerThreads.WorkerThreadYear;
import com.example.riji.Month_related.Month;
import com.example.riji.Year_related.Year;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class YearActivity extends AppCompatActivity implements WorkerThreadYear.Callback {

    private final List<Month> mMonth = new ArrayList<>();
    float x1, x2, y1, y2;
    //get current time
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    //getTime() returns the current date in default time zone
    int year = calendar.get(Calendar.YEAR);
    private MonthListAdapter mAdapter;
    private String mString;
    private WorkerThreadYear mWorkerThread;
    private int year_id;
    private Year year1;
    //Button myButton = new Button(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);

        yearBackTable();

        // Get a handle to the RecyclerView.
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new MonthListAdapter(this, mMonth, this.getApplication());

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bund = getIntent().getExtras();
        //get the current year and month
        if (bund != null) {
            year = bund.getInt("year");
        }

        TextView theMonth = findViewById(R.id.two019);

        String ye = Integer.toString(year);
        theMonth.setText(ye);

        mWorkerThread = new WorkerThreadYear(new Handler(), this, this);
        mWorkerThread.start();
        mWorkerThread.prepareHandlerMonths();
        mWorkerThread.queueMonths(year);
        SearchView sv = findViewById(R.id.search_bar);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bund = new Bundle();
                bund.putString("term","%"+query+"%");

                Intent i = new Intent(YearActivity.this, SearchActivity.class);
                i.putExtras(bund);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void yearToday(View view) {
        startActivity(new Intent(YearActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

    public void january(View view) {

        startActivity(new Intent(YearActivity.this, MonthActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void yearBackTable() {
        Button backButton = findViewById(R.id.TableofYear);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(YearActivity.this, TableofYear.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

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
                    Bundle bund = new Bundle();
                    bund.putInt("year", year);
                    bund.putInt("month", 1);

                    Intent i = new Intent(YearActivity.this, MonthActivity.class);
                    i.putExtras(bund);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                if (x1 < x2) {
                    Intent j = new Intent(YearActivity.this, TableofYear.class);
                    startActivity(j);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        mWorkerThread.quit();
        super.onDestroy();
    }

    @Override
    public void onMonthsFound(LiveData<List<Month>> months) {
        months.observe(this, new Observer<List<Month>>() {
            @Override
            public void onChanged(@Nullable List<Month> months) {
                // Update the cached copy of days
                mAdapter.setMonth(months);
            }
        });
    }

    @Override
    public void onYearFound(Year year, int year_id) {
        //  year1 = year;
        //  this.year_id = year_id;
        //  mWorkerThread.prepareHandlerMonths();
        //  mWorkerThread.queueMonths(year1.getYear());
    }
}
