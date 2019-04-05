package com.example.riji;

import androidx.room.Dao;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = { BulletPoint.class, Day.class },
        version = 1)
public abstract class Database extends RoomDatabase {

    public abstract BulletPointDAO getBulletPointDAO();
    public abstract DayDAO getDayDAO();
}
