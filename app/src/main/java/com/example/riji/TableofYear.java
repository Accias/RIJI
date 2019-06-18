package com.example.riji;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.Adapters.WordListAdapter;
import com.example.riji.Adapters.YearListAdapter;
import com.example.riji.HandlerThreads.WorkerThreadTableOfYears;
import com.example.riji.Year_related.Year;
import com.example.riji.Year_related.YearViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TableofYear extends AppCompatActivity implements WorkerThreadTableOfYears.Callback, YearListAdapter.OnNoteListener {

    private final List<Year> mYear = new ArrayList<>();
    float x1, x2, y1, y2;
    YearViewModel mYViewModel;
    private YearListAdapter mAdapter;
    final Database rijiDatabase = Database.getDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableof_year);

        // Get a handle to the RecyclerView.
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new YearListAdapter(this, mYear,this);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mYViewModel = ViewModelProviders.of(this).get(YearViewModel.class);

        final Button addYear = findViewById(R.id.addYear);
        addYear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0,
                        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Database.newYear(rijiDatabase);
                    }
                });
            }
        });

        WorkerThreadTableOfYears mWorkerThread = new WorkerThreadTableOfYears(new Handler(), this, this);
        mWorkerThread.start();
        mWorkerThread.prepareHandlerYears();
        mWorkerThread.queueYears();

        SearchView sv = findViewById(R.id.search_bar);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bund = new Bundle();
                bund.putString("term","%"+query+"%");

                Intent i = new Intent(TableofYear.this, SearchActivity.class);
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

    public void tableToday(View view) {
        startActivity(new Intent(TableofYear.this, TableofYear.class));
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
                    Bundle bund = new Bundle();
                    bund.putInt("year", 2019);
                    bund.putInt("month", 1);

                    Intent i = new Intent(TableofYear.this, YearActivity.class);
                    i.putExtras(bund);
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

    @Override
    public void onNoteClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TableofYear.this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this year?");

        //inflate the dialogue with the layout in the xml activity_display_message
        LayoutInflater layoutInflater = LayoutInflater.from(TableofYear.this);

        @SuppressLint("InflateParams") View popupInputDialogView = layoutInflater.inflate(R.layout.wordlist_item, null);
        builder.setView(popupInputDialogView);

        // Set up the buttons
        final AlertDialog dialog = builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.deleteYear(position, rijiDatabase);
                Toast toast = Toast.makeText(TableofYear.this, "Deleted", Toast.LENGTH_SHORT);
                toast.show();
                dialog.cancel();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(TableofYear.this, "Not deleted", Toast.LENGTH_SHORT);
                toast.show();
                dialog.cancel();
            }
        }).create();

        //2. now setup to change color of the button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(TableofYear.this, R.color.black));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(TableofYear.this, R.color.black));
            }
        });
        dialog.show();
    }
}
