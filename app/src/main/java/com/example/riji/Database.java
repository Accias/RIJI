package com.example.riji;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointDAO;
import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;

import java.util.Calendar;
import java.util.TimeZone;

/*
    This is the room database class. It is where the DAOs are instantiated, and the initial data
    is stored in the database.
 */

@androidx.room.Database(entities = {BulletPoint.class, Day.class},
        version = 1)
public abstract class Database extends RoomDatabase {
    //Only one instance of the database can be initialized at a time.
    private static volatile Database INSTANCE;

    public abstract BulletPointDAO getBulletPointDAO();

    public abstract DayDAO getDayDAO();

    //creates a database if there isn't one, returns it if it is already created.
    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "database").addCallback(sRoomDatabaseCallback).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //executes the populate database method upon opening the app
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    //insert test data into the database
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final BulletPointDAO mBPDao;
        private final DayDAO mDayDao;

        PopulateDbAsync(Database db) {
            mBPDao = db.getBulletPointDAO();
            mDayDao = db.getDayDAO();
        }

        //database tasks must be done on a separate thread so they don't clog up the main thread and freeze the UI.
        @Override
        protected Void doInBackground(final Void... params) {
            //get current date
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            //getTime() returns the current date in default time zone
            int day = calendar.get(Calendar.DATE);
            //Note: +1 the month for current month
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            //wipe previous data in the database
            mBPDao.deleteAll();
            //mDayDao.deleteAll();
            //initialize new Day class based on current date
           // Day day1 = new Day(day,month, year,dayOfWeek);
          //  mDayDao.insertDay(day1);


            long id = mDayDao.getDayId(2019,5,3);
            //insert test bulletpoints
            BulletPoint bp = new BulletPoint(0, "Hello",id);
            mBPDao.insertBulletPoint(bp);
            bp = new BulletPoint(0, "World",id);
            mBPDao.insertBulletPoint(bp);

            return null;
        }
    }
}
