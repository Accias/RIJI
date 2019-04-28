package com.example.riji;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@androidx.room.Database(entities = {BulletPoint.class, Day.class},
        version = 1)
public abstract class Database extends RoomDatabase {
    private static volatile Database INSTANCE;

    public abstract BulletPointDAO getBulletPointDAO();

    public abstract DayDAO getDayDAO();

    static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "database").addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final BulletPointDAO mBPDao;
        private final DayDAO mDayDao;

        PopulateDbAsync(Database db) {
            mBPDao = db.getBulletPointDAO();
            mDayDao = db.getDayDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            //getTime() returns the current date in default time zone
            int day = calendar.get(Calendar.DATE);
            //Note: +1 the month for current month
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            mBPDao.deleteAll();
            mDayDao.deleteAll();
            Day day1 = new Day(day,month, year,dayOfWeek);
            mDayDao.insertDay(day1);

            BulletPoint bp = new BulletPoint(0, "Hello");
            mBPDao.insertBulletPoint(bp);
            bp = new BulletPoint(0, "World");
            mBPDao.insertBulletPoint(bp);

            return null;
        }
    }
}
