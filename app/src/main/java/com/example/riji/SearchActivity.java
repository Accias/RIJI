package com.example.riji;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riji.Adapters.SearchListAdapter;
import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.Day_related.Day;
import com.example.riji.HandlerThreads.WorkerThreadSearch;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements WorkerThreadSearch.Callback {
    private RecyclerView mRecyclerView;
    private SearchListAdapter mAdapter;
    private List<BulletPoint> mSearch = new ArrayList<>();
    private List<Day> mDays = new ArrayList<>();
    private String query;
    private WorkerThreadSearch mWorkerThread;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        back();

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new SearchListAdapter(this, mSearch, mDays);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecor);

        //get the current year and month from the bundle passed by the intent
        Bundle bund = getIntent().getExtras();
        //only set the variables if the bundle is not null, to prevent errors on startup when no bundle is passed.
        if (bund != null) {
            query = bund.getString("term");
            //start querying Day
            mWorkerThread = new WorkerThreadSearch(new Handler(), this, this);
            mWorkerThread.start();
            mWorkerThread.prepareHandlerSearch();
            mWorkerThread.queueSearch(query);
        }

        SearchView sv = findViewById(R.id.searchItem);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bund = new Bundle();
                bund.putString("term", "%" + query + "%");

                Intent i = new Intent(SearchActivity.this, SearchActivity.class);
                i.putExtras(bund);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void searchToday(View view) {
        startActivity(new Intent(SearchActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

    public void back() {
        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                finish();
            }
        });
    }

    @Override
    public void onDayFound(Day day, long day_id) {
        mDays.add(day);
        counter++;
        if (counter < mSearch.size()) {
            mWorkerThread.prepareHandlerDay();
            mWorkerThread.queueDay((int) mSearch.get(counter).getDay_id());
        } else if (counter == mSearch.size()) {
            mAdapter.setDays(mDays);
            mAdapter.setBulletPoints(mSearch);
        }
    }

    @Override
    public void onSearchFound(List<BulletPoint> bullets) {
        counter = 0;
        mSearch = bullets;

        if (bullets.size() != 0) {
            mWorkerThread.prepareHandlerDay();
            mWorkerThread.queueDay((int) bullets.get(counter).getDay_id());
        } else {
            Toast toast = Toast.makeText(SearchActivity.this, "No results found.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
