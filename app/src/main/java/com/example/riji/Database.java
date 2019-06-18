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
import com.example.riji.Month_related.Month;
import com.example.riji.Month_related.MonthDAO;
import com.example.riji.Year_related.Year;
import com.example.riji.Year_related.YearDAO;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/*
    This is the room database class. It is where the DAOs are instantiated, and the initial data
    is stored in the database.
 */

@androidx.room.Database(entities = {BulletPoint.class, Day.class, Month.class, Year.class},
        version = 3)
public abstract class Database extends RoomDatabase {
    //Only one instance of the database can be initialized at a time.
    private static volatile Database INSTANCE;
    //executes the populate database method upon opening the app
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute(INSTANCE);
                }
            };

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

    static void newYear(Database db) {
        YearDAO mYearDao = db.getYearDAO();
        //get the last year in the database
        List<Year> years = mYearDao.getAllYearsNoLive();
        Year lastYear = years.get(years.size() - 1);
        //use calendar to get next year in case someone changes the time system a few decades from now.
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(lastYear.getYear(), 1, 1);
        calendar.add(Calendar.YEAR, 1);
        int year = calendar.get(Calendar.YEAR);
        //call genYear method
        genYear(db, year);
    }

    /*
        Generate a new year of entities.
     */
    private static void genYear(Database db, int year) {

        //get all DAOs
        BulletPointDAO mBPDao = db.getBulletPointDAO();
        DayDAO mDayDao = db.getDayDAO();
        MonthDAO mMonthDao = db.getMonthDAO();
        YearDAO mYearDao = db.getYearDAO();

        //create new year :)
        Year year1 = new Year(year);
        mYearDao.insertYear(year1);
        long yearId = mYearDao.getYearId(year);

        //for loop to generate all days and months
        for (int i = 1; i <= 12; i++) {
            Month month = new Month(i, year, yearId);
            mMonthDao.insertMonth(month);
            long monthId = mMonthDao.getMonthId(year, i);

            int iMonth = i - 1; // 1 (months begin with 0)
            int iDay = 1;
            // Create a calendar object and set year and month
            Calendar mycal = new GregorianCalendar(year, iMonth, iDay);
            // Get the number of days in that month
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28

            for (int j = 1; j <= daysInMonth; j++) {
                mycal = new GregorianCalendar(year, iMonth, j);
                int weekDate = mycal.get(Calendar.DAY_OF_WEEK);
                Day day1 = new Day(j, i, year, weekDate, monthId);
                mDayDao.insertDay(day1);
            }

        }
    }

    public abstract BulletPointDAO getBulletPointDAO();

    public abstract DayDAO getDayDAO();

    public abstract MonthDAO getMonthDAO();

    public abstract YearDAO getYearDAO();

    //insert test data into the database
    private static class PopulateDbAsync extends AsyncTask<Database, Void, Void> {

        private final DayDAO mDayDao;

        PopulateDbAsync(Database db) {
            mDayDao = db.getDayDAO();
        }

        //database tasks must be done on a separate thread so they don't clog up the main thread and freeze the UI.
        protected Void doInBackground(final Database... params) {
            //get current date
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            //getTime() returns the current date in default time zone
            int year = calendar.get(Calendar.YEAR);

            //initialize new Day class based on current date if there isn't one
            if (mDayDao.findSpecificDayNoLive(year, 1, 1) == null) {
                genYear(params[0], year);
            }

            return null;
        }

    }
}
