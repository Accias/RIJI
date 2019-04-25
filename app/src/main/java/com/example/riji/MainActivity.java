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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final List<BulletPoint> mBulletPoints = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    private static final String DATABASE_NAME = "riji_database";
    private Database rijiDatabase;
    private String mString = "";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private BulletPointViewModel mBPViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        rijiDatabase = Room.databaseBuilder(getApplicationContext(), Database.class, DATABASE_NAME).build();
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
               // if(DayDAO.findSpecificDay(2019, 4, 21)==null)
              //  Movies movie =new Movies();
                //movie.setMovieId( “2”);
                //movie.setMovieName(“The Prestige”);
                //movieDatabase.daoAccess () . insertOnlySingleMovie (movie);
            }
        }) .start();*/


        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this, mBulletPoints);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBPViewModel = ViewModelProviders.of(this).get(BulletPointViewModel.class);
        mBPViewModel.getAllBulletPoints().observe(this, new Observer<List<BulletPoint>>() {
            @Override
            public void onChanged(@Nullable final List<BulletPoint> bulletPoints) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setBulletPoints(bulletPoints);
            }
        });

        final Button addBullet = findViewById(R.id.addBullet);
        addBullet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Bullet Point");

                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                @SuppressLint("InflateParams") View popupInputDialogView = layoutInflater.inflate(R.layout.activity_display_message, null);
                builder.setView(popupInputDialogView);
                final EditText bullet = popupInputDialogView.findViewById(R.id.bullet);

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
                            mBPViewModel.insert(new BulletPoint(0,mString));
                        }
                        finish();
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
        Toast toast = Toast.makeText(MainActivity.this, "event", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void sendNote(View view) {
        Toast toast = Toast.makeText(MainActivity.this, "note", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void sendTask(View view) {
        Toast toast = Toast.makeText(MainActivity.this, "task", Toast.LENGTH_SHORT);
        toast.show();
    }
    /*public void sendMessage(View view) {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            EditText editText = (EditText) findViewById(R.id.editText);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
    }*/
}