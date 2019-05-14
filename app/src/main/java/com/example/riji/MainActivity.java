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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointDAO;
import com.example.riji.BulletPoint_related.BulletPointViewModel;
import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;
import com.example.riji.Day_related.DayViewModel;

import java.text.DateFormat;
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
    private DayViewModel mDayViewModel;
    private DayDAO mDayDao;
    private BulletPointDAO mBPDao;
    long id;
    //private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0,
    //        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private Handler mUiHandler = new Handler();
    private MyWorkerThread mWorkerThread;

    //get current time
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    //getTime() returns the current date in default time zone
    final int day = calendar.get(Calendar.DATE);

    //Note: +1 the month for current month
    final int month = calendar.get(Calendar.MONTH) + 1;
    final int year = calendar.get(Calendar.YEAR);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    final Date date = new Date(year, month, day);

    //set up dialogue
    private TextView symbol;
    private int bulletType = 0;
    private Day day1;
    float x1, x2, y1, y2;

    public MainActivity() {
        mString = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Database rijiDatabase = Database.getDatabase(this);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String currrentDate = DateFormat.getDateInstance().format(calendar.getTime());
        TextView dateview = findViewById(R.id.tuesday1_2);
        dateview.setText(currrentDate);

        //getTime() returns the current date in default time zone
        final int day = calendar.get(Calendar.DATE);
        //Note: +1 the month for current month
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final Date date = new Date(year, month, day);

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

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeAndCallBack(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        //find current day class
        mDayDao = rijiDatabase.getDayDAO();
        mBPDao = rijiDatabase.getBulletPointDAO();

        mWorkerThread = new MyWorkerThread(new Handler(), this, mDayDao, mBPDao);
        mWorkerThread.start();
        mWorkerThread.prepareHandlerDay();
        mWorkerThread.queueDay(year, month, day);


        /* = new MyWorkerThread("myWorkerThread");
        Runnable task = new Runnable() {
            Day theDay;

            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                theDay = mDayDao.findSpecificDayNoLive(year, month, day);
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "Background task is completed",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        };
        mWorkerThread.start();
        mWorkerThread.prepareHandler();
        mWorkerThread.postTask(task);*/

        //mDayViewModel = ViewModelProviders.of(this).get(DayViewModel.class);
        /*mDayViewModel.getSpecificDay(year,month,day).observe(this, new Observer<Day>() {
            @Override
            public void onChanged(@Nullable final Day day2) {
               day1=day2;
            }
        });*/
        //threadPoolExecutor.execute(getRunnable(i));

        //back button method
        dayBackMonth();

        /*mBPViewModel.getSpecificDayBulletPoints(day1.day).observe(this, new Observer<List<BulletPoint>>() {
            @Override
            public void onChanged(@Nullable final List<BulletPoint> bulletPoints) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setBulletPoints(bulletPoints);
            }
        });*/


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
        startActivity(new Intent(MainActivity.this, Month_swipe.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

    //how to go from one class to another class
    public void dayBackMonth() {
        Button backButton = findViewById(R.id.jan);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //use an intent to allow the classes to interchange
                startActivity(new Intent(MainActivity.this, Month_swipe.class));
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
                    Intent intent = new Intent(MainActivity.this, Month_swipe.class);
                    Bundle bund = new Bundle();
                    bund.putInt("year", year);
                    bund.putInt("month", month);
                    intent.putExtras(bund);
                    startActivity(intent);

                    //switch activities
                    Intent j = new Intent(MainActivity.this, Month_swipe.class);
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
        day1 = day;
        id = day_id;
        mWorkerThread.prepareHandlerBP();
        mWorkerThread.queueBP(id);
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
        //BulletPoint element = mBulletPoints.get(mPosition);

        //mBulletPoints.set(mPosition,element);


        // Intent intent = new Intent(this, NewActivity.java);
    }
}


// ----------------------------------------------------------


    /*public void sendMessage(View view) {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            EditText editText = (EditText) findViewById(R.id.editText);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
    }*/

