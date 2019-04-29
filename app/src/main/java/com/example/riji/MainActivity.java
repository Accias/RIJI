package com.example.riji;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointViewModel;
import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity{
    private final List<BulletPoint> mBulletPoints = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    private static final String DATABASE_NAME = "riji_database";
    private Database rijiDatabase;
    private String mString = "";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private BulletPointViewModel mBPViewModel;
    private TextView symbol;
    private int bulletType = 0;
    private DayDAO mDayDao;
    private Day day1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        rijiDatabase = Room.databaseBuilder(getApplicationContext(), Database.class, DATABASE_NAME).build();

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this, mBulletPoints);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //find current day class
        mDayDao = rijiDatabase.getDayDAO();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //getTime() returns the current date in default time zone
        final int day = calendar.get(Calendar.DATE);
        //Note: +1 the month for current month
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Date date = new Date(year,month,day);

        findSpecificDayAsyncTask asyncTask = (findSpecificDayAsyncTask) new findSpecificDayAsyncTask(mDayDao,new findSpecificDayAsyncTask.AsyncResponse(){

            @Override
            public void processFinish(Day output){
                day1=output;
            }
        }).execute(date);

      //  Toast toast = Toast.makeText(MainActivity.this, day1.getDay(), Toast.LENGTH_LONG);

        mBPViewModel = ViewModelProviders.of(this).get(BulletPointViewModel.class);

        mBPViewModel.getAllBulletPoints().observe(this, new Observer<List<BulletPoint>>() {
            @Override
            public void onChanged(@Nullable final List<BulletPoint> bulletPoints) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setBulletPoints(bulletPoints);
            }
        });

        //BACK BUTTON METHOD
        Back();

        final Button addBullet = findViewById(R.id.addBullet);
        addBullet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Bullet Point");

                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                @SuppressLint("InflateParams") View popupInputDialogView = layoutInflater.inflate(R.layout.activity_display_message, null);
                builder.setView(popupInputDialogView);
                final EditText bullet = popupInputDialogView.findViewById(R.id.bullet);
                symbol = popupInputDialogView.findViewById(R.id.symbol);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(bullet.getText())) {
                            dialog.dismiss();
                            Toast toast = Toast.makeText(MainActivity.this, "Cannot store empty string.", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            dialog.dismiss();
                            mString = bullet.getText().toString();
                            mBPViewModel.insert(new BulletPoint(bulletType, symbol.getText() + " " + mString));
                        }
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void sendEvent(View view) {
        bulletType = 0;
        Toast toast = Toast.makeText(MainActivity.this, "o", Toast.LENGTH_SHORT);
        toast.show();
        symbol.setText(" o ");

    }

    public void sendNote(View view) {
        bulletType = 1;
        Toast toast = Toast.makeText(MainActivity.this, "-", Toast.LENGTH_SHORT);
        toast.show();
        symbol.setText(" - ");

    }

    public void sendTask(View view) {
        bulletType = 2;
        Toast toast = Toast.makeText(MainActivity.this, "~", Toast.LENGTH_SHORT);
        toast.show();
        symbol.setText(" ~ ");
    }

    public void Back()
    {
       Button backButton = (Button) findViewById(R.id.jan);
       backButton.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(MainActivity.this, Month.class));
           }
       });

       }
    }


// ----------------------------------------------------------


    /** Called when the user taps the Send button */
    /*public void sendMessage(View view) {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            EditText editText = (EditText) findViewById(R.id.editText);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
    }*/

