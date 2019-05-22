package com.example.riji;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointViewModel;
import com.example.riji.Day_related.Day;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements MyWorkerThread.Callback, WordListAdapter.onNoteListener {
    private final List<BulletPoint> mBulletPoints = new ArrayList<>();
    private WordListAdapter mAdapter;
    private String mString;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private BulletPointViewModel mBPViewModel;
    long id;
    Day day1;
    //private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0,
    //        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private MyWorkerThread mWorkerThread;

    //get current time
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    //getTime() returns the current date in default time zone
    int day = calendar.get(Calendar.DATE);
    //Note: +1 the month for current month
    int month = calendar.get(Calendar.MONTH) + 1;
    int year = calendar.get(Calendar.YEAR);

    //set up dialogue
    private TextView symbol;
    private int bulletType = 0;
    float x1, x2, y1, y2;

    public MainActivity() {
        mString = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Database rijiDatabase = Database.getDatabase(this);

        mBPViewModel = ViewModelProviders.of(this).get(BulletPointViewModel.class);

        // Get a handle to the RecyclerView.
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this, mBulletPoints, this);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecor);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndCallBack(mAdapter));
        //itemTouchHelper.attachToRecyclerView(mRecyclerView);

        Bundle bund = getIntent().getExtras();
        //get the current year and month
        if (bund != null) {
            year = bund.getInt("year");
            month = bund.getInt("month");
            day = bund.getInt("day");
        }

        mWorkerThread = new MyWorkerThread(new Handler(), this, this);
        mWorkerThread.start();
        mWorkerThread.prepareHandlerDay();
        mWorkerThread.queueDay(year, month, day);

        //back button method
        dayBackMonth();
        backButton();
        //allow user to add a new bullet point
        final Button addBullet = findViewById(R.id.addBullet);
        addBullet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //employ an alert dialogue, not simply a dialogue(imagine these as pop up window)
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Bullet Point");

                //inflate the dialogue with the layout in the xml activity_display_message
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

                @SuppressLint("InflateParams") View popupInputDialogView = layoutInflater.inflate(R.layout.activity_display_message, null);
                builder.setView(popupInputDialogView);
                final EditText bullet = popupInputDialogView.findViewById(R.id.bullet);
                symbol = popupInputDialogView.findViewById(R.id.symbol);

                // Set up the buttons
                final AlertDialog dialog = builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(bullet.getText())) {
                            dialog.dismiss();
                            Toast toast = Toast.makeText(MainActivity.this, "Cannot store empty string.", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            dialog.dismiss();
                            mString = bullet.getText().toString();
                            //what shows on the screen
                            mBPViewModel.insert(new BulletPoint(bulletType, mString, id));
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();

                //2. now setup to change color of the button
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mWorkerThread.quit();
        super.onDestroy();
    }

    //when the user chooses the event button
    public void sendEvent(View view) {
        bulletType = 0;
        Toast toast = Toast.makeText(MainActivity.this, " ○ ", Toast.LENGTH_SHORT);
        toast.show();
        symbol.setText(" ○ ");
    }

    //when the user chooses the note button
    public void sendNote(View view) {
        bulletType = 1;
        Toast toast = Toast.makeText(MainActivity.this, " - ", Toast.LENGTH_SHORT);
        toast.show();
        symbol.setText(" - ");
    }

    //when the user chooses the task button
    public void sendTask(View view) {
        bulletType = 2;
        Toast toast = Toast.makeText(MainActivity.this, " • ", Toast.LENGTH_SHORT);
        toast.show();
        symbol.setText(" • ");
    }

    public void monthToday(View view) {
        startActivity(new Intent(MainActivity.this, MonthActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

    //how to go from one class to another class
    public void dayBackMonth() {
        Button backButton = findViewById(R.id.jan);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert year and month data to be transfered to MonthActivity class
                Bundle bund = new Bundle();
                bund.putInt("year", year);
                bund.putInt("month", month);
                Intent intent = new Intent(MainActivity.this, MonthActivity.class);
                intent.putExtras(bund);
                //use an intent to allow the classes to interchange
                startActivity(intent);
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
                if (x1 < x2) {
                    //insert year and month data to be transfered to MonthActivity class
                    Bundle bund = new Bundle();
                    bund.putInt("year", year);
                    bund.putInt("month", month);

                    //switch activities
                    Intent j = new Intent(MainActivity.this, MonthActivity.class);
                    j.putExtras(bund);
                    startActivity(j);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
                break;
        }
        return false;
    }


    @Override
    public void onDayFound(Day day, long day_id) {
        day1=day;
        id = day_id;
        int weekdate=day.getWeekDate();
        TextView date = findViewById(R.id.tuesday1_2);
        switch (month)
        {
            case 1:
                date.setText(weekday(weekdate) + ", January "+ this.day);
                break;
            case 2:
                date.setText(weekday(weekdate) + ", Feburary "+ this.day);
                break;
            case 3:
                date.setText(weekday(weekdate) + ", March "+this.day);
                break;
            case 4:
                date.setText(weekday(weekdate) + ", April "+ this.day);
                break;
            case 5:
                date.setText(weekday(weekdate) + ", May "+ this.day);
                break;
            case 6:
                date.setText(weekday(weekdate) + ", June "+ this.day);
                break;
            case 7:
                date.setText(weekday(weekdate) + ", July "+ this.day);
                break;
            case 8:
                date.setText(weekday(weekdate) + ", August "+ this.day);
                break;
            case 9:
                date.setText(weekday(weekdate) + ", September "+ this.day);
                break;
            case 10:
                date.setText(weekday(weekdate) + ", October "+ this.day);
                break;
            case 11:
                date.setText(weekday(weekdate) + ", November "+ this.day);
                break;
            case 12:
                date.setText(weekday(weekdate) + ", December "+ this.day);
                break;
        }

        mWorkerThread.prepareHandlerBP();
        mWorkerThread.queueBP(id);
    }

    public String weekday(int day)
    {
        String name = new String();
        switch (day)
        {
            case 1:
                 name ="Sunday";
                break;
            case 2:
                name = "Monday";
                break;
            case 3:
                name = "Tuesday";
                break;
            case 4:
                name = "Wednesday";
                break;
            case 5:
                name = "Thursday";
                break;
            case 6:
                name = "Friday";
                break;
            case 7:
                name = "Saturday";
                break;
        }return name;
    }

    public void backButton()
    {
        Button back = findViewById(R.id.jan);
        switch(month)
        {
            case 1:
                back.setText("JAN");
                break;
            case 2:
                back.setText("FEV");
                break;
            case 3:
                back.setText("MAR");
                break;
            case 4:
                back.setText("ARI");
                break;
            case 5:
                back.setText("MAY");
                break;
            case 6:
                back.setText("JUN");
                break;
            case 7:
                back.setText("JUL");
                break;
            case 8:
                back.setText("AUG");
                break;
            case 9:
                back.setText("SEP");
                break;
            case 10:
                back.setText("OCT");
                break;
            case 11:
                back.setText("NOV");
                break;
            case 12:
                back.setText("DEC");
                break;

        }
    }

    @Override
    public void onBPFound(LiveData<List<BulletPoint>> bullets) {
        bullets.observe(this, new Observer<List<BulletPoint>>() {
            @Override
            public void onChanged(@Nullable final List<BulletPoint> bulletPoints) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setBulletPoints(bulletPoints);
            }
        });
    }


    @Override
    public void onNoteClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this bullet point?");

        //inflate the dialogue with the layout in the xml activity_display_message
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

        @SuppressLint("InflateParams") View popupInputDialogView = layoutInflater.inflate(R.layout.wordlist_item, null);
        builder.setView(popupInputDialogView);

        // Set up the buttons
        final AlertDialog dialog = builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.deleteBP(position, mBPViewModel.getApplication());
                Toast toast = Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT);
                toast.show();
                dialog.cancel();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(MainActivity.this, "not deleted", Toast.LENGTH_SHORT);
                toast.show();
                dialog.cancel();
            }
        }).create();

        //2. now setup to change color of the button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
            }
        });
        dialog.show();
    }
}


