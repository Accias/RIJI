package com.example.riji;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
                            Database.class, "database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
