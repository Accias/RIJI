package com.example.riji;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = { BulletPoint.class, Day.class },
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
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final BulletPointDAO mDao;

        PopulateDbAsync(Database db) {
            mDao = db.getBulletPointDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            BulletPoint bp = new BulletPoint(0,"Hello");
            mDao.insertBulletPoint(bp);
            bp = new BulletPoint(0,"World");
            mDao.insertBulletPoint(bp);
            return null;
        }
    }
}
